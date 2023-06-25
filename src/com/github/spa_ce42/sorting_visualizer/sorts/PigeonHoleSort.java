package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class PigeonHoleSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        pigeonhole_sort(va.size());
        h.clear();
    }

    public static void pigeonhole_sort(int n) {
        int min = h.get(0);
        int max = h.get(0);
        int range, i, j, index;

        for(int a = 0; a < n; a++) {
            sleep();
            if(h.get(a) > max)
                max = h.get(a);
            if(h.get(a) < min)
                min = h.get(a);
        }

        range = max - min + 1;
        int[] phole = new int[range];

        for(i = 0; i < n; i++) {
            sleep();
            phole[h.get(i) - min]++;
        }


        index = 0;

        for(j = 0; j < range; j++)
            while(phole[j]-- > 0) {
                sleep();
                h.set(index++, j + min);
            }
    }
}