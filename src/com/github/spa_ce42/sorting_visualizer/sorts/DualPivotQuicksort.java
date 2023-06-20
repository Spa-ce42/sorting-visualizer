package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

public class DualPivotQuicksort {
    private static Highlighter h;
    private static final long SLEEP_NANOS = 30000;

    static void dualPivotQuickSort(int low, int high)
    {
        if (low < high)
        {

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(low, high);

            dualPivotQuickSort(low, piv[0] - 1);
            dualPivotQuickSort(piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(piv[1] + 1, high);
        }
    }

    static int[] partition(int low, int high)
    {
        if (h.get(low) > h.get(high))
            h.swap(low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = h.get(low), q = h.get(high);

        while (k <= g)
        {
            long expectedTime = System.nanoTime() + SLEEP_NANOS;

            // If elements are less than the left pivot
            if (h.get(k) < p)
            {
                h.swap(k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (h.get(k) >= q)
            {
                while (h.get(g) > q && k < g)
                    g--;

                h.swap(k, g);
                g--;

                if (h.get(k) < p)
                {
                    h.swap(k, j);
                    j++;
                }
            }
            k++;

            while(true) {
                if(System.nanoTime() > expectedTime) {
                    break;
                }
            }
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        h.swap(low, j);
        h.swap(high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        dualPivotQuickSort(0, va.size() - 1);
    }
}
