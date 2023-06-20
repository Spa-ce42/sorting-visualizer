package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

public class BubbleSort {
    private static Highlighter h;
    private static final long SLEEP_NANOS = 0;

    private static void sleep() {
        long expectedTime = System.nanoTime() + SLEEP_NANOS;
        while(true) {
            if(System.nanoTime() > expectedTime) {
                break;
            }
        }
    }

    static void bubbleSort(int n)
    {
        int i, j;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                sleep();
                if (h.get(j) > h.get(j + 1)) {
                    // Swap arr[j] and arr[j+1]
                    h.swap(j, j + 1);
                    swapped = true;
                }
            }

            // If no two elements were
            // swapped by inner loop, then break
            if (!swapped)
                break;
        }
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        bubbleSort(va.size());
    }
}
