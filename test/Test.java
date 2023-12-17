import sortingvisualizer.SV;
import java.util.Random;

public class Test extends SV {
    public static void main(String[] args) {
        SV.launch();
    }

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
        if(get(low) > get(high)) {
            swap(low, high);
        }

        int j = low + 1;
        int g = high - 1, k = low + 1, p = get(low), q = get(high);
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

            k++;

            delay(10);
            update();
        }

        j--;
        g++;

        swap(low, j);
        swap(high, g);

        return new int[] {j, g};
    }

    void shuffle() {
        Random r = new Random();
        for(int i = 0, n = length(); i < n; ++i) {
            int j = r.nextInt(i, n);
            swap(i, j);
            delay(10);
            update();
        }
    }

    @Override
    public void run() {
        {
            int[] a = new int[200];

            for(int i = 0; i < a.length; ++i) {
                a[i] = i + 1;
            }

            array(a);
        }

        delay(1000);
        shuffle();
        delay(1000);
        dualPivotQuickSort(0, length() - 1);
        clearColor();
    }
}
