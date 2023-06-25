package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.MAX_HIGHLIGHTS;

public class SelectionSort {
    private static Highlighter h;

    static void sort(int n) {
        for(int i = 0, j = n - 1; i < j; ++i) {
            int min = Integer.MAX_VALUE;
            int minIndex = i + 1;

            for(int k = i + 1; k < n; ++k) {
                int l = h.get(k);

                if(min > l) {
                    min = l;
                    minIndex = k;
                }
            }

            h.swap(minIndex, i);
        }
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, MAX_HIGHLIGHTS);
        sort(va.size());
        h.clear();
    }
}
