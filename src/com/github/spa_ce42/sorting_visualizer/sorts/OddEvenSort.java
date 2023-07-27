package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

public class OddEvenSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        oddEvenSort(va.size());
        h.clear();
    }

    public static void oddEvenSort(int n)
    {
        boolean isSorted = false; // Initially array is unsorted

        while (!isSorted)
        {
            isSorted = true;

            // Perform Bubble sort on odd indexed element
            for (int i=1; i<=n-2; i=i+2)
            {
                if (h.get(i) > h.get(i+1))
                {
                    h.swap(i, i + 1);
                    isSorted = false;
                }
            }

            // Perform Bubble sort on even indexed element
            for (int i=0; i<=n-2; i=i+2)
            {
                if (h.get(i) > h.get(i+1))
                {
                    h.swap(i, i + 1);
                    isSorted = false;
                }
            }
        }
    }
}
