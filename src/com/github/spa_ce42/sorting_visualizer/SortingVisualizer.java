package com.github.spa_ce42.sorting_visualizer;

import com.github.spa_ce42.sorting_visualizer.internal.Renderer;
import com.github.spa_ce42.sorting_visualizer.internal.Window;
import com.github.spa_ce42.sorting_visualizer.sorts.BitonicSort;
import com.github.spa_ce42.sorting_visualizer.sorts.BubbleSort;
import com.github.spa_ce42.sorting_visualizer.sorts.DualPivotQuicksort;
import com.github.spa_ce42.sorting_visualizer.sorts.HeapSort;
import com.github.spa_ce42.sorting_visualizer.sorts.InsertionSort;
import com.github.spa_ce42.sorting_visualizer.sorts.Mergesort;
import com.github.spa_ce42.sorting_visualizer.sorts.Metadata;
import com.github.spa_ce42.sorting_visualizer.sorts.OddEvenSort;
import com.github.spa_ce42.sorting_visualizer.sorts.PancakeSort;
import com.github.spa_ce42.sorting_visualizer.sorts.PigeonHoleSort;
import com.github.spa_ce42.sorting_visualizer.sorts.Quicksort;
import com.github.spa_ce42.sorting_visualizer.sorts.QuicksortWithSpecialPivot;
import com.github.spa_ce42.sorting_visualizer.sorts.RadixSort;
import com.github.spa_ce42.sorting_visualizer.sorts.SelectionSort;
import com.github.spa_ce42.sorting_visualizer.sorts.ShellSort;
import com.github.spa_ce42.sorting_visualizer.sorts.TimSort;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.spa_ce42.sorting_visualizer.sorts.Metadata.sleep;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SortingVisualizer {
    private final Window window;
    private final boolean shouldRun;
    private final VArray va;

    public SortingVisualizer() {
        this.window = new Window(1280, 720, "Sorting Visualizer", NULL, NULL);
        this.shouldRun = true;
        Renderer.initialize(10000);
        this.va = new VArray(1280, 720, 1 << 14);
        this.window.addWindowSizeCallback((l, i, i1) ->  {
            glViewport(0, 0, i, i1);
            SortingVisualizer.this.va.resize(i, i1);
        });
    }

    public void start() {
        long l, m;

        while(!this.window.shouldClose() && this.shouldRun) {
            l = System.nanoTime();
            this.window.clear(GL_COLOR_BUFFER_BIT);

            this.va.draw();

            this.window.update();

            m = System.nanoTime();

            this.window.setTitle("Program | " + (1000000000 / (m - l) + " fps"));
        }

        this.window.dispose();
    }

    private static void shuffle(VArray va, Random r) {
        int lastI = 0;
        int lastJ = 0;

        for(int i = 0, n = va.size(); i < n; ++i) {
            long expectedNano = System.nanoTime() + Metadata.SLEEP_NANOS;

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

    public static void check(VArray va) {
        for(int i = 0, n = va.size() - 1; i < n; ++i) {
            sleep();
            va.setColor(i, 1, 0, 0);
            va.setColor(i + 1, 1, 0, 0);
            int j = va.get(i);
            int k = va.get(i + 1);

            if(j <= k) {
                va.setColor(i, 0, 1, 0);
                va.setColor(i + 1, 0, 1, 0);
            }
        }
    }

    public static void main(String[] args) {
        AtomicReference<SortingVisualizer> sv = new AtomicReference<>();
        CyclicBarrier cb = new CyclicBarrier(2);
        Thread t = new Thread(() -> {
            try {
                cb.await();


                VArray va = sv.get().va;
                Random r = new Random();

                while(sv.get().shouldRun) {
                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    double d = glfwGetTime();
                    DualPivotQuicksort.sort(va);
                    double e = glfwGetTime();

                    System.out.println(e - d);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    BitonicSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    PigeonHoleSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    ShellSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    RadixSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    QuicksortWithSpecialPivot.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    HeapSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    TimSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    Mergesort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    Quicksort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    SelectionSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    InsertionSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    PancakeSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    OddEvenSort.sort(va);
                    check(va);

                    Thread.sleep(1000);
                    shuffle(va, r);
                    Thread.sleep(1000);
                    BubbleSort.sort(va);
                    check(va);
                }

            } catch(BrokenBarrierException | InterruptedException ignored) {
                //continue
            }
        });

        t.setDaemon(true);
        t.start();
        sv.set(new SortingVisualizer());

        try {
            cb.await();
        } catch(InterruptedException | BrokenBarrierException ignored) {
            //continue
        }

        sv.get().start();


    }
}
