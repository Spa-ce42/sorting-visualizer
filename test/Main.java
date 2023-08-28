import com.kennethfei.sorting_visualizer.SV;

import java.util.Random;

public class Main extends SV {
    // quicksort the subarray a[lo .. hi] using dual-pivot quicksort
    private void sort(int lo, int hi) {
        if(hi <= lo) return;

        // make sure a[lo] <= a[hi]
        if(get(hi) < get(lo)) {
            swap(lo, hi);
            update();
        }

        int lt = lo + 1, gt = hi - 1;
        int i = lo + 1;

        while(i <= gt) {

            if(get(i) < get(lo)) {
                swap(lt++, i++);
            } else if(get(hi) < get(i)) {
                swap(i, gt--);
            } else {
                i++;
            }

            update();
        }

        swap(lo, --lt);
        swap(hi, ++gt);

        // recursively sort three subarrays
        sort(lo, lt - 1);
        if(get(lt) < get(gt)) {
            sort(lt + 1, gt - 1);
        }

        sort(gt + 1, hi);
    }

    @Override
    public void run() {
        final int N = 1 << 14;
        setMaxColoredBars(N / 100);
        setUpdateInterval(3);
        int[] a = new int[N];

        for(int i = 0; i < a.length; ++i) {
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
        sort(0, length() - 1);
        clearColor();
        forceUpdate();
    }

    public static void main(String[] args) {
        launch();
    }
}
