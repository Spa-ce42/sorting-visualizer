import java.util.Arrays;
import java.util.Random;

public class QuicksortWithSpecialPivot {
    private static Random r = new Random();

    static void insertionSort(int[] a, int low, int high) {
        for (int i = low + 1; i < high; ++i) {
            int key = a[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j = j - 1;
            }

            a[j + 1] = key;
        }
    }

    static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    static void sort(int[] arr, int low, int high) {
        if(high - low < 17) {
            insertionSort(arr, low, high);
            return;
        }

        int a, b, c, d, e;
        a = arr[r.nextInt(high - low) + low];
        b = arr[r.nextInt(high - low) + low];
        c = arr[r.nextInt(high - low) + low];
        d = arr[r.nextInt(high - low) + low];
        e = arr[r.nextInt(high - low) + low];
        double m = (double)(a + b + c + d + e) / 5d;

        int i = low;
        int j = high - 1;
        while(i <= j) {
            int k = arr[i];

            if(k <= m) {
                ++i;
                continue;
            }

            swap(arr, i, j);
            j--;
        }

        sort(arr, low, i);
        sort(arr, i, high);
    }

    public static void main(String[] args) {
        int[] a = new int[100];

        for(int i = 0; i < a.length; ++i) {
            a[i] = i;
        }

        Random r = new Random();

        for(int i = 0; i < a.length; ++i) {
            int j = r.nextInt(a.length - i) + i;
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        System.out.println(Arrays.toString(a));

        sort(a, 0, a.length);
        System.out.println(Arrays.toString(a));
    }
}
