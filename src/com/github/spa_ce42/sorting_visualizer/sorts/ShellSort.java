package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class ShellSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        sort(va.size());
        h.clear();
    }

    static void sort(int n)
    {

        // Start with a big gap, then reduce the gap
        for (int gap = n/2; gap > 0; gap /= 2)
        {
            // Do a gapped insertion sort for this gap size.
            // The first gap elements a[0..gap-1] are already
            // in gapped order keep adding one more element
            // until the entire array is gap sorted
            for (int i = gap; i < n; i += 1)
            {
                sleep();
                // add a[i] to the elements that have been gap
                // sorted save a[i] in temp and make a hole at
                // position i
                int temp = h.get(i);

                // shift earlier gap-sorted elements up until
                // the correct location for a[i] is found
                int j;
                for (j = i; j >= gap && h.get(j - gap) > temp; j -= gap)
                    h.set(j, h.get(j - gap));

                // put temp (the original a[i]) in its correct
                // location
                h.set(j, temp);
            }
        }
    }

}
