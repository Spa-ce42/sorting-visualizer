package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

public class PancakeSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        pancakeSort(va.size());
        h.clear();
    }

    static void flip(int i)
    {
        int start = 0;
        while (start < i)
        {
            h.swap(start, i);
            start++;
            i--;
        }
    }

    // Returns index of the
    // maximum element in
    // arr[0..n-1]
    static int findMax(int n)
    {
        int mi, i;
        for (mi = 0, i = 0; i < n; ++i) {
            if(h.get(i) > h.get(mi)) {
                mi = i;
            }
        }
        return mi;
    }

    // The main function that
    // sorts given array using
    // flip operations
    static int pancakeSort(int n)
    {
        // Start from the complete
        // array and one by one
        // reduce current size by one
        for (int curr_size = n; curr_size > 1;
             --curr_size)
        {
            // Find index of the
            // maximum element in
            // arr[0..curr_size-1]
            int mi = findMax(curr_size);

            // Move the maximum element
            // to end of current array
            // if it's not already at
            // the end
            if (mi != curr_size-1)
            {
                // To move at the end,
                // first move maximum
                // number to beginning
                flip(mi);

                // Now move the maximum
                // number to end by
                // reversing current array
                flip(curr_size-1);
            }
        }
        return 0;
    }
}
