package com.github.spa_ce42.sorting_visualizer;

import com.github.spa_ce42.sorting_visualizer.internal.Window;
import com.github.spa_ce42.sorting_visualizer.sorts.BubbleSort;
import com.github.spa_ce42.sorting_visualizer.sorts.DualPivotQuicksort;
import com.github.spa_ce42.sorting_visualizer.sorts.Mergesort;
import com.github.spa_ce42.sorting_visualizer.sorts.Quicksort;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SortingVisualizer {
    private final Window window;
    private final boolean shouldRun;
    private final VArray va;

    public SortingVisualizer() {
        this.window = new Window(1280, 720, "Program", NULL, NULL);
        this.shouldRun = true;
        this.va = new VArray(1 << 14);
    }

    public void start() {
        while(!this.window.shouldClose() && this.shouldRun) {
            this.window.clear(GL_COLOR_BUFFER_BIT);
            this.va.draw();
            this.window.update();
        }

        this.window.dispose();
    }

    private static final long SHUFFLE_SLEEP_NANOS = 35000;
    private static void shuffle(VArray va, Random r) {
        int lastI = 0;
        int lastJ = 0;

        for(int i = 0, n = va.size(); i < n; ++i) {
            long expectedNano = System.nanoTime() + SHUFFLE_SLEEP_NANOS;

            va.setColor(i, 1, 0, 0);
            int j = r.nextInt(n - i) + i;
            va.setColor(j, 1, 0, 0);
            va.swap(i, r.nextInt(n - i) + i);
            va.setColor(lastI, 1, 1, 1);
            va.setColor(lastJ, 1, 1, 1);
            lastI = i;
            lastJ = j;

            while(true) {
                if(System.nanoTime() > expectedNano) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicReference<SortingVisualizer> sv = new AtomicReference<>();
        CyclicBarrier cb = new CyclicBarrier(2);
        Thread t = new Thread(() -> {
            sv.set(new SortingVisualizer());

            try {
                cb.await();
            } catch(InterruptedException | BrokenBarrierException ignored) {
                //continue
            }

            sv.get().start();
        });

        t.start();

        try {
            cb.await();
        } catch(BrokenBarrierException ignored) {
            //continue
        }

        VArray va = sv.get().va;
        Random r = new Random();
        Thread.sleep(1000);
        shuffle(va, r);
        Thread.sleep(1000);
        BubbleSort.sort(va);

        Thread.sleep(1000);
        shuffle(va, r);
        Thread.sleep(1000);
        Mergesort.sort(va);

        Thread.sleep(1000);
        shuffle(va, r);
        Thread.sleep(1000);
        Quicksort.sort(va);

        Thread.sleep(1000);
        shuffle(va, r);
        Thread.sleep(1000);
        DualPivotQuicksort.sort(va);
    }
}
