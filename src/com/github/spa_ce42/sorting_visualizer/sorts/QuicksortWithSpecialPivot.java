package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import java.util.Random;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class QuicksortWithSpecialPivot {
    private static Highlighter h;
    private static final Random r = new Random();

    static void sort(int low, int high) {
        if(high - low < 17) {
            InsertionSort.sort(h.getVArray(), low, high);
            return;
        }

        int a, b, c, d, e, l = high - low;
        a = h.get(low);
        b = h.get(low + (int)(0.25 * l));
        c = h.get(low + (int)(0.5 * l));
        d = h.get(low + (int)(0.75 * l));
        e = h.get(high - 1);
        double m = (double)(a + b + c + d + e) / 5d;

        int i = low;
        int j = high - 1;

        while(i <= j) {
            sleep();
            int k = h.get(i);

            if(k <= m) {
                ++i;
                continue;
            }

            h.swap(i, j);
            --j;
        }

        sort(low, i);
        sort(i, high);
    }

    public static void sort(VArray va) {
        h = new Highlighter(va, Metadata.MAX_HIGHLIGHTS);
        sort(0, va.size());
        h.clear();
    }
}
