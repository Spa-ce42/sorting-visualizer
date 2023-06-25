package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class BitonicSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        sort(va.size(), 1);
        h.clear();
    }
    /* The parameter dir indicates the sorting direction,
   ASCENDING or DESCENDING; if (a[i] > a[j]) agrees
   with the direction, then a[i] and a[j] are
   interchanged. */
    static void compAndSwap(int i, int j, int dir)
    {
        sleep();
        if ( (h.get(i) > h.get(j) && dir == 1) ||
             (h.get(i) < h.get(j) && dir == 0))
        {
            h.swap(i, j);
        }
    }

    /* It recursively sorts a bitonic sequence in ascending
       order, if dir = 1, and in descending order otherwise
       (means dir=0). The sequence to be sorted starts at
       index position low, the parameter cnt is the number
       of elements to be sorted.*/
    static void bitonicMerge(int low, int cnt, int dir)
    {
        if (cnt>1)
        {
            int k = cnt/2;
            for (int i=low; i<low+k; i++)
                compAndSwap(i, i+k, dir);
            bitonicMerge(low, k, dir);
            bitonicMerge(low+k, k, dir);
        }
    }

    /* This function first produces a bitonic sequence by
       recursively sorting its two halves in opposite sorting
       orders, and then  calls bitonicMerge to make them in
       the same order */
    static void bitonicSort(int low, int cnt, int dir)
    {
        if (cnt>1)
        {
            int k = cnt/2;

            // sort in ascending order since dir here is 1
            bitonicSort(low, k, 1);

            // sort in descending order since dir here is 0
            bitonicSort(low+k, k, 0);

            // Will merge whole sequence in ascending order
            // since dir=1.
            bitonicMerge(low, cnt, dir);
        }
    }

    /*Caller of bitonicSort for sorting the entire array
      of length N in ASCENDING order */
    static void sort(int N, int up)
    {
        bitonicSort(0, N, up);
    }
}
