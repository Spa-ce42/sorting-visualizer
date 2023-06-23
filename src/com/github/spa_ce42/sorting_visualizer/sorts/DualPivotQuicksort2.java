package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.SLEEP_NANOS;

public class DualPivotQuicksort2 {
    private static Highlighter h;

    public static void sort(VArray a) {
        h = new Highlighter(a, 10);
        sort(0, a.size() - 1);
    }


    private static void sleep() {
        long expectedTime = System.nanoTime() + SLEEP_NANOS;
        while(true) {
            if(System.nanoTime() > expectedTime) {
                break;
            }
        }
    }

    // quicksort the subarray a[lo .. hi] using dual-pivot quicksort
    private static void sort(int lo, int hi) {
        if (hi <= lo) return;

        // make sure a[lo] <= a[hi]
        if (less(h.get(hi), h.get(lo))) h.swap(lo, hi);

        int lt = lo + 1, gt = hi - 1;
        int i = lo + 1;
        while (i <= gt) {
            sleep();
            if       (less(h.get(i), h.get(lo))) h.swap(lt++, i++);
            else if  (less(h.get(hi), h.get(i))) h.swap(i, gt--);
            else                         i++;
        }
        h.swap(lo, --lt);
        h.swap(hi, ++gt);

        // recursively sort three subarrays
        sort(lo, lt-1);
        if (less(h.get(lt), h.get(gt))) sort(lt+1, gt-1);
        sort(gt+1, hi);
    }



    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(int v, int w) {
        return v < w;
    }
}
