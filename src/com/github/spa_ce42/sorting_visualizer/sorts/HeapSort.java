package com.github.spa_ce42.sorting_visualizer.sorts;

import com.github.spa_ce42.sorting_visualizer.Highlighter;
import com.github.spa_ce42.sorting_visualizer.VArray;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;

public class HeapSort {
    private static Highlighter h;

    public static void sort(VArray va) {
        h = new Highlighter(va, 10);
        sort(va.size());
    }

    static void sort(int N)
    {
        // Build heap (rearrange array)
        for (int i = N / 2 - 1; i >= 0; i--) {
            heapify(N, i);
        }

        // One by one extract an element from heap
        for (int i = N - 1; i > 0; i--) {
            h.swap(0, i);

            // call max heapify on the reduced heap
            heapify(i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    static void heapify(int N, int i)
    {
        sleep();
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < N && h.get(l) > h.get(largest))
            largest = l;

        // If right child is larger than largest so far
        if (r < N && h.get(r) > h.get(largest))
            largest = r;

        // If largest is not root
        if (largest != i) {
            h.swap(i, largest);

            // Recursively heapify the affected sub-tree
            heapify(N, largest);
        }
    }
}
