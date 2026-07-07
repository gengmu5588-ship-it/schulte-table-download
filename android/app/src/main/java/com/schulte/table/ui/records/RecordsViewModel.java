package com.schulte.table.ui.records;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.schulte.table.data.entity.RecordEntity;
import com.schulte.table.data.repository.RecordRepository;

import java.util.List;

public class RecordsViewModel extends AndroidViewModel {

    private final RecordRepository recordRepository;
    private final MutableLiveData<Integer> filterGridSize = new MutableLiveData<>(0);
    private final LiveData<List<RecordEntity>> filteredRecords;

    public RecordsViewModel(@NonNull Application application) {
        super(application);
        recordRepository = new RecordRepository(application);

        filteredRecords = Transformations.switchMap(filterGridSize, size -> {
            if (size == 0) {
                return recordRepository.getAllRecords();
            } else {
                return recordRepository.getRecordsByGridSize(size);
            }
        });
    }

    public LiveData<List<RecordEntity>> getFilteredRecords() {
        return filteredRecords;
    }

    public void setFilterGridSize(int size) {
        filterGridSize.setValue(size);
    }

    public int getFilterGridSize() {
        Integer val = filterGridSize.getValue();
        return val != null ? val : 0;
    }

    public void loadBestTime(int gridSize, RecordRepository.BestTimeCallback callback) {
        recordRepository.getBestTime(gridSize, callback);
    }
}
