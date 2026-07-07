package com.schulte.table.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridGenerator {

    public static List<Integer> generate(int gridSize) {
        int total = gridSize * gridSize;
        List<Integer> numbers = new ArrayList<>(total);
        for (int i = 1; i <= total; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, new Random());
        return numbers;
    }
}
