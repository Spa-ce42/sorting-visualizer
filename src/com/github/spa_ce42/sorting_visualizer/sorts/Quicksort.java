package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.SLEEP_NANOS;

public class Quicksort {
    private static Highlighter h;

    private static int partition(int low, int high)
    {
        int pivot = h.get(high);

        // Index of smaller element and indicates
        // the right position of pivot found so far
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            long expectedNanos = System.nanoTime() + SLEEP_NANOS;

            // If current element is smaller than the pivot
            if (h.get(j) < pivot) {

                // Increment index of smaller element
                i++;
                h.swap(i, j);
            }
            while(true) {
                if(System.nanoTime() > expectedNanos) {
                    break;
                }
            }
        }

        h.swap(i + 1, high);
        return (i + 1);
    }

    // The main function that implements QuickSort
    // arr[] --> Array to be sorted,
    // low --> Starting index,
    // high --> Ending index
    private static void quickSort( int low, int high)
    {
        if (low < high) {

            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = partition(low, high);

            // Separately sort elements before
            // partition and after partition
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        quickSort(0, va.size() - 1);
    }
}
