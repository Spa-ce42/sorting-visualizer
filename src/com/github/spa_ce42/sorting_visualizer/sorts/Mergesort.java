package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

public class Mergesort {
    private static Highlighter h;
    private static final long SLEEP_NANOS = 30000;

    private static void sleep() {
        long expectedTime = System.nanoTime() + SLEEP_NANOS;
        while(true) {
            if(System.nanoTime() > expectedTime) {
                break;
            }
        }
    }

    static void merge(int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int L[] = new int[n1];
        int R[] = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i) {
            sleep();
            L[i] = h.get(l + i);
        }

        for (int j = 0; j < n2; ++j) {
            sleep();
            R[j] = h.get(m + 1 + j);
        }

        // Merge the temp arrays

        // Initial indices of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            sleep();

            if (L[i] <= R[j]) {
                h.set(k, L[i]);
                i++;
            }
            else {
                h.set(k, R[j]);
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            sleep();
            h.set(k, L[i]);
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            sleep();
            h.set(k, R[j]);
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    static void sort(int l, int r)
    {
        if (l < r) {

            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(l, m);
            sort(m + 1, r);

            // Merge the sorted halves
            merge(l, m, r);
        }
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        sort(0, va.size() - 1);
    }
}
