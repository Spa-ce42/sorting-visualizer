package com.kennethfei.sorting_visualizer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

import com.kennethfei.sorting_visualizer.internal.HistogramRenderer;
import imgui.ImGui;
import imgui.type.ImInt;
import java.util.Random;

public class Main extends SV {
    private final String[] combo = {
            "Select...",
            "Bubble Sort",
            "Selection Sort",
            "Dual-Pivot Quick Sort"
    };
    private final ImInt i = new ImInt(0);

    public static void main(String[] args) {
        SV.launch();
    }

    @Override
    public void init(SVConfiguration svConfig) {
        svConfig.setWidth(1920);
        svConfig.setHeight(1080);
    }

    private void shuffle() {
        Random r = new Random();

        for(int i = 1, n = length(); i < n; ++i) {
            int j = r.nextInt(i);
            swap(i, j);
            update();
            delay(10);
        }

        clearColor();
        forceUpdate();
    }

    private void bubbleSort() {
        int n = length();
        for(int i = n; i >= 0; --i) {
            for(int j = 1; j < i; ++j) {
                if(get(j - 1) > get(j)) {
                    swap(j - 1, j);
                    update();
                    delay(1);
                }
            }
        }
    }

    private void insertionSort() {
        int n = length();
        for(int i = 1; i < n; ++i) {
            int key = get(i);
            int j = i - 1;

            while(j >= 0 && get(j) > key) {
                set(j + 1, get(j));
                j = j - 1;

                update();
                delay(1);
            }

            set(j + 1, key);
        }
    }

    private void dualPivotQuickSort(int low, int high) {
        if(low < high) {
            int[] piv;
            piv = dualPivotPartition(low, high);

            dualPivotQuickSort(low, piv[0] - 1);
            dualPivotQuickSort(piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(piv[1] + 1, high);
        }
    }

    private int[] dualPivotPartition(int low, int high) {
        if(get(low) > get(high)) {
            swap(low, high);
        }

        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = get(low), q = get(high);

        while(k <= g) {
            if(get(k) < p) {
                swap(k, j);
                j++;
            } else if(get(k) >= q) {
                while(get(g) > q && k < g) {
                    g--;
                }

                swap(k, g);
                g--;

                if(get(k) < p) {
                    swap(k, j);
                    j++;
                }
            }

            delay(10);
            update();
            k++;
        }
        j--;
        g++;

        swap(low, j);
        swap(high, g);

        return new int[] {j, g};
    }

    @Override
    public void run() {
        int[] a = new int[100];

        for(int i = 0; i < a.length; ++i) {
            a[i] = i + 1;
        }

        array(a);

        while(!super.window.shouldClose()) {
            super.window.clear(GL_COLOR_BUFFER_BIT);
            if(this.i.get() != 0) {
                shuffle();
                delay(1000);

                switch(this.i.get()) {
                    case 1 -> {
                        bubbleSort();
                    }

                    case 2 -> {
                        insertionSort();
                    }

                    case 3 -> {
                        dualPivotQuickSort(0, length() - 1);
                    }
                }

                this.i.set(0);
                clearColor();
                forceUpdate();
            }


            this.drawImGui();
            HistogramRenderer.render();
            super.window.update();
        }
    }

    @Override
    public void onImGuiRender() {
        ImGui.begin("Settings");
        if(ImGui.combo("Sorting Algorithms", this.i, this.combo));
        ImGui.end();
    }
}
