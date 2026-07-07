package com.schulte.table.ui.home.training;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.schulte.table.data.entity.RecordEntity;
import com.schulte.table.data.repository.RecordRepository;
import com.schulte.table.util.BenchmarkUtil;
import com.schulte.table.util.GridGenerator;
import com.schulte.table.util.TimerHelper;
import com.schulte.table.util.VibrationHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrainingViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> gridSize = new MutableLiveData<>(5);
    private final MutableLiveData<List<Integer>> gridNumbers = new MutableLiveData<>();
    private final MutableLiveData<Integer> nextNumber = new MutableLiveData<>(1);
    private final MutableLiveData<Long> elapsedMillis = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isComplete = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> mistakes = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> wrongCellIndex = new MutableLiveData<>(-1);
    private final MutableLiveData<Long> finalTime = new MutableLiveData<>(0L);
    private final MutableLiveData<Long> bestTime = new MutableLiveData<>(null);

    private final Set<Integer> correctSet = new HashSet<>();
    private final TimerHelper timer = new TimerHelper();
    private final RecordRepository recordRepository;

    private android.os.Handler timerHandler;
    private Runnable timerRunnable;

    public TrainingViewModel(@NonNull Application application) {
        super(application);
        recordRepository = new RecordRepository(application);
        timerHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        timerRunnable = () -> {
            if (timer.isRunning()) {
                elapsedMillis.postValue(timer.getElapsedMillis());
                timerHandler.postDelayed(timerRunnable, 50);
            }
        };
        // Initialize with sequential numbers for initial display
        Integer size = gridSize.getValue();
        if (size != null) {
            gridNumbers.setValue(generateSequentialNumbers(size));
        }
    }

    public MutableLiveData<Integer> getGridSize() { return gridSize; }
    public MutableLiveData<List<Integer>> getGridNumbers() { return gridNumbers; }
    public MutableLiveData<Integer> getNextNumber() { return nextNumber; }
    public MutableLiveData<Long> getElapsedMillis() { return elapsedMillis; }
    public MutableLiveData<Boolean> getIsRunning() { return isRunning; }
    public MutableLiveData<Boolean> getIsComplete() { return isComplete; }
    public MutableLiveData<Integer> getMistakes() { return mistakes; }
    public MutableLiveData<Integer> getWrongCellIndex() { return wrongCellIndex; }
    public MutableLiveData<Long> getFinalTime() { return finalTime; }
    public MutableLiveData<Long> getBestTime() { return bestTime; }
    public Set<Integer> getCorrectSet() { return correctSet; }

    public void setGridSize(int size) {
        if (size >= 3 && size <= 10) {
            gridSize.setValue(size);
            if (!timer.isRunning()) {
                resetGame();
            }
        }
    }

    // Generate sequential numbers for initial display (1, 2, 3, ..., N)
    private List<Integer> generateSequentialNumbers(int size) {
        List<Integer> numbers = new ArrayList<>();
        int total = size * size;
        for (int i = 1; i <= total; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public void startNewGame() {
        int size = gridSize.getValue() != null ? gridSize.getValue() : 5;

        // Generate random numbers for training
        List<Integer> numbers = GridGenerator.generate(size);
        gridNumbers.setValue(numbers);
        nextNumber.setValue(1);
        mistakes.setValue(0);
        isComplete.setValue(false);
        wrongCellIndex.setValue(-1);
        finalTime.setValue(0L);
        correctSet.clear();

        timer.reset();
        timer.start();
        isRunning.setValue(true);

        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.post(timerRunnable);

        loadBestTime(size);
    }

    public void onCellClick(int index, int number) {
        Boolean running = isRunning.getValue();
        if (running == null || !running) return;

        Integer next = nextNumber.getValue();
        if (next == null) return;

        if (number == next) {
            VibrationHelper.vibrateCorrect(getApplication());
            correctSet.add(number);
            nextNumber.setValue(number + 1);

            Integer size = gridSize.getValue();
            if (size != null && number == size * size) {
                timer.stop();
                long elapsed = timer.getElapsedMillis();
                finalTime.setValue(elapsed);
                elapsedMillis.setValue(elapsed);
                timerHandler.removeCallbacks(timerRunnable);
                isRunning.setValue(false);
                isComplete.setValue(true);
                saveRecord(elapsed);
            }
        } else {
            VibrationHelper.vibrateWrong(getApplication());
            Integer m = mistakes.getValue();
            mistakes.setValue(m != null ? m + 1 : 1);
            wrongCellIndex.setValue(index);
            timerHandler.postDelayed(() -> wrongCellIndex.setValue(-1), 300);
        }
    }

    private void saveRecord(long elapsed) {
        Integer size = gridSize.getValue();
        Integer m = mistakes.getValue();
        if (size == null) return;

        RecordEntity record = new RecordEntity();
        record.gridSize = size;
        record.totalNumbers = size * size;
        record.elapsedMillis = elapsed;
        record.completedAt = System.currentTimeMillis();
        record.mistakes = m != null ? m : 0;
        record.isBest = false;

        recordRepository.insertRecord(record);
        loadBestTime(size);
    }

    private void loadBestTime(int size) {
        recordRepository.getBestTime(size, best -> bestTime.postValue(best));
    }

    // Stop game without saving the record
    public void stopGameWithoutSave() {
        timerHandler.removeCallbacks(timerRunnable);
        timer.stop();
        isRunning.setValue(false);
        isComplete.setValue(false);
        // Don't set finalTime, don't save record
        
        // Reset to sequential numbers
        Integer size = gridSize.getValue();
        if (size != null) {
            gridNumbers.setValue(generateSequentialNumbers(size));
        }
        nextNumber.setValue(1);
        mistakes.setValue(0);
        wrongCellIndex.setValue(-1);
        correctSet.clear();
    }

    public void resetGame() {
        timerHandler.removeCallbacks(timerRunnable);
        timer.reset();
        isRunning.setValue(false);
        isComplete.setValue(false);
        elapsedMillis.setValue(0L);
        nextNumber.setValue(1);
        mistakes.setValue(0);
        wrongCellIndex.setValue(-1);
        finalTime.setValue(0L);
        correctSet.clear();
        
        // Show sequential numbers before training starts
        Integer size = gridSize.getValue();
        if (size != null) {
            gridNumbers.setValue(generateSequentialNumbers(size));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timerHandler.removeCallbacks(timerRunnable);
        timer.stop();
    }
}
