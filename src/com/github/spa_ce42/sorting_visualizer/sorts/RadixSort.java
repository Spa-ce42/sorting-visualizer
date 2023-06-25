package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class RadixSort {
    private static Highlighter h;

    // A utility function to get maximum value in arr[]
    static int getMax(int n)
    {
        int mx = h.get(0);
        for (int i = 1; i < n; i++)
            if (h.get(i) > mx)
                mx = h.get(i);
        return mx;
    }

    // A function to do counting sort of arr[] according to
    // the digit represented by exp.
    static void countSort(int n, int exp)
    {
        int output[] = new int[n]; // output array
        int i;
        int count[] = new int[10];

        // Store count of occurrences in count[]
        for (i = 0; i < n; i++)
        {
            sleep();
            count[(h.get(i) / exp) % 10]++;
        }

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++)
        {
            sleep();
            count[i] += count[i - 1];
        }

        // Build the output array
        for (i = n - 1; i >= 0; i--) {
            sleep();
            output[count[(h.get(i) / exp) % 10] - 1] = h.get(i);
            count[(h.get(i) / exp) % 10]--;
        }

        // Copy the output array to arr[], so that arr[] now
        // contains sorted numbers according to current
        // digit
        for (i = 0; i < n; i++) {
            sleep();
            h.set(i, output[i]);
        }
    }

    // The main function to that sorts arr[] of
    // size n using Radix Sort
    static void radixsort(int n)
    {
        // Find the maximum number to know number of digits
        int m = getMax(n);

        // Do counting sort for every digit. Note that
        // instead of passing digit number, exp is passed.
        // exp is 10^i where i is current digit number
        for (int exp = 1; m / exp > 0; exp *= 10)
            countSort(n, exp);
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        radixsort(va.size());
        h.clear();
    }
}
