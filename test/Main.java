import com.kennethfei.sorting_visualizer.SV;
import com.kennethfei.sorting_visualizer.SVConfiguration;

import java.util.Random;

public class Main extends SV {
    void dualPivotQuickSort(int low, int high) {
        if(low < high) {
            int[] piv;
            piv = partition(low, high);
            dualPivotQuickSort(low, piv[0] - 1);
            dualPivotQuickSort(piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(piv[1] + 1, high);
        }
    }

    int[] partition(int low, int high) {
        if (get(low) > get(high)) {
            swap(low, high);
        }

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1, p = get(low), q = get(high);

        while (k <= g) {
            if (get(k) < p) {
                swap(k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (get(k) >= q)
            {
                while (get(g) > q && k < g)
                    g--;

                swap(k, g);
                g--;

                if (get(k) < p)
                {
                    swap(k, j);
                    j++;
                }
            }
            k++;
            update();
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(low, j);
        swap(high, g);
        update();

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    @Override
    public void init(SVConfiguration svConfig) {
        svConfig.setWidth(1280);
        svConfig.setHeight(720);
        svConfig.setMaxColoredBars((1 << 14) / 100);
        svConfig.setUpdateInterval(2);
    }

    @Override
    public void run() {
        final int MAX_VALUE = 1 << 14;
        int[] a = new int[MAX_VALUE];

        for(int i = 0; i < MAX_VALUE; ++i) {
            a[i] = i + 1;
        }

        array(a);
        delay(1000);
        Random r = new Random();

        for(int i = 0, n = length(); i < n; ++i) {
            int j = r.nextInt(i, n);
            swap(i, j);
            update();
        }

        clearColor();
        forceUpdate();

        delay(1000);
        setAutoColor(true);
        dualPivotQuickSort(0, length() - 1);
        clearColor();
        forceUpdate();
    }

    public static void main(String[] args) {
        launch();
    }
}
