package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.SLEEP_NANOS;

public class ThreeWayQuicksort {
    private static Highlighter h;
    static int i, j;


    private static void sleep() {
        long expectedTime = System.nanoTime() + SLEEP_NANOS;
        while(true) {
            if(System.nanoTime() > expectedTime) {
                break;
            }
        }
    }

    /* This function partitions a[] in three parts
       a) a[l..i] contains all elements smaller than pivot
       b) a[i+1..j-1] contains all occurrences of pivot
       c) a[j..r] contains all elements greater than pivot */
    static void partition(int l, int r)
    {

        i = l - 1; j = r;
        int p = l - 1, q = r;
        int v = h.get(r);

        while (true) {

            // From left, find the first element greater than
            // or equal to v. This loop will definitely
            // terminate as v is last element
            while(h.get(++i) < v) {
                sleep();
            ;
            }

            // From right, find the first element smaller than
            // or equal to v
            while (v < h.get(--j)) {
                sleep();
                if(j == l)
                    break;
            }

            // If i and j cross, then we are done
            if (i >= j)
                break;

            // Swap, so that smaller goes on left greater goes
            // on right
            h.swap(i, j);

            // Move all same left occurrence of pivot to
            // beginning of array and keep count using p
            if (h.get(i) == v) {
                p++;
                h.swap(i, p);
            }

            // Move all same right occurrence of pivot to end of
            // array and keep count using q
            if (h.get(j) == v) {
                q--;
                h.swap(j, q);
            }
        }

        // Move pivot element to its correct index
        h.swap(i, r);

        // Move all left same occurrences from beginning
        // to adjacent to arr[i]
        j = i - 1;
        for (int k = l; k < p; k++, j--)
        {
            sleep();
            h.swap(j, k);
        }

        // Move all right same occurrences from end
        // to adjacent to arr[i]
        i = i + 1;
        for (int k = r - 1; k > q; k--, i++)
        {
            sleep();
            h.swap(i, k);
        }
    }

    // 3-way partition based quick sort
    static void quicksort(int l, int r)
    {
        if (r <= l)
            return;

        i = 0; j = 0;

        // Note that i and j are passed as reference
        partition(l, r);

        // Recur
        quicksort(l, j);
        quicksort(i, r);
    }

    public static void sort(VArray a) {
        h = new Highlighter(a, 10);
        quicksort(0, a.size() - 1);
    }
}
