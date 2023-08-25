import com.kennethfei.sorting_visualizer.SV;

import java.util.Random;

public class Main extends SV {
    void dualPivotQuickSort(int low, int high)
    {
        if (low < high)
        {

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(low, high);

            dualPivotQuickSort(low, piv[0] - 1);
            dualPivotQuickSort(piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(piv[1] + 1, high);
        }
    }

    int[] partition(int low, int high)
    {
        if (arrayGet(low) > arrayGet(high)) {
            arraySwap(low, high);
        }

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arrayGet(low), q = arrayGet(high);

        while (k <= g)
        {
            delay(20);
            // If elements are less than the left pivot
            if (arrayGet(k) < p)
            {
                arraySwap(k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (arrayGet(k) >= q)
            {
                while (arrayGet(g) > q && k < g)
                    g--;

                arraySwap(k, g);
                g--;

                if (arrayGet(k) < p)
                {
                    arraySwap(k, j);
                    j++;
                }
            }
            k++;
            update();
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        arraySwap(low, j);
        arraySwap(high, g);
        update();

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    @Override
    public void run() {
        int[] a = new int[100];

        for(int i = 0; i < 100; ++i) {
            a[i] = i + 1;
        }

        array(a, 100);
        delay(1000);
        Random r = new Random();
        setUpdateInterval(1);
        setMaxColoredBars(2);

        for(int i = 0, n = arrayLength(); i < n; ++i) {
            int j = r.nextInt(i, n);
            arraySwap(i, j);
            update();
            delay(20);
        }

        clearColor();
        forceUpdate();

        delay(1000);
        setAutoColor(true);
        dualPivotQuickSort(0, arrayLength() - 1);
        clearColor();
        forceUpdate();
    }

    public static void main(String[] args) {
        launch();
    }
}
