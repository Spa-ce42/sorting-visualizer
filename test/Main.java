import com.kennethfei.sorting_visualizer.SV;

import java.util.Random;

public class Main extends SV {
    @Override
    public void run() {
        int[] a = new int[10000];

        for(int i = 0; i < 10000; ++i) {
            a[i] = i + 1;
        }

        array(a, 10000);
        Random r = new Random();
        setUpdateInterval(2);

        for(int i = 0, n = arrayLength(); i < n; ++i) {
            int j = r.nextInt(i, n);
            arraySwap(i, j);
            color(i, 1f, 0f, 0f);
            color(j, 1f, 0f, 0f);
            update();
        }

        forceUpdate();
    }

    public static void main(String[] args) {
        launch();
    }
}
