package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.MAX_HIGHLIGHTS;

public class InsertionSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, MAX_HIGHLIGHTS);
        sort(va.size());
        h.clear();
    }

    static void sort(int low, int high) {
        for(int i = low + 1; i < high; ++i) {
            int key = h.get(i);
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while(j >= 0 && h.get(j) > key) {
                h.set(j + 1, h.get(j));
                j = j - 1;
            }

            h.set(j + 1, key);
        }
    }

    public static void sort(VArray va, int low, int high) {
        h = new Highlighter(va, MAX_HIGHLIGHTS);
        sort(low, high);
        h.clear();
    }

    static void sort(int n) {
        for(int i = 1; i < n; ++i) {
            int key = h.get(i);
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while(j >= 0 && h.get(j) > key) {
                h.set(j + 1, h.get(j));
                j = j - 1;
            }

            h.set(j + 1, key);
        }
    }
}
