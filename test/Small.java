import com.kennethfei.sorting_visualizer.SV;

public class Small extends SV {
    @Override
    public void run() {
        int[] a = new int[128];
        for(int i = 0; i < a.length; ++i) {
            a[i] = i * i;
        }
        array(a);
    }

    public static void main(String[] args) {
        launch();
    }
}
