import java.util.Random;

public class QuickSortVisualizer {

    private final SkyscrapersUi ui;

    private final boolean initialDescending;
    private boolean isDescending;
    private final int thresholdValue;
    private final int maxValue;

    public QuickSortVisualizer(SkyscrapersUi ui, int thresholdValue, int maxValue, boolean initialDescending) {
        this.ui = ui;
        this.thresholdValue = thresholdValue;
        this.maxValue = maxValue;
        this.initialDescending = initialDescending;
    }

    // ==================== ГЕНЕРАЦИЯ ЧИСЕЛ ====================
    public int[] generateRandomNumbers(int count) {
        isDescending = initialDescending;
        Random random = new Random();
        int[] arr = new int[count];

        // Гарантируем хотя бы одно число <= minValue
        arr[0] = random.nextInt(thresholdValue) + 1; // от 1 до minValue

        for (int i = 1; i < count; i++) {
            arr[i] = random.nextInt(maxValue) + 1;  // от 1 до maxValue
        }
        return arr;
    }

    // ==================== ОСНОВНАЯ СОРТИРОВКА ====================
    public int[] sort(int[] arr) {
        if (arr == null || arr.length == 0)
            return new int[0];

        int[] arrClone = arr.clone();
        String direction = isDescending ? "ПО УБЫВАНИЮ" : "ПО ВОЗРАСТАНИЮ";
        ui.showMessage("=== Запуск QuickSort " + direction + " ===");
        quickSort(arrClone, 0, arrClone.length - 1);

        // Переключаем направление
        isDescending = !isDescending;

        ui.clearSortScreen();
        return arrClone;
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = isDescending ? (arr[j] > pivot) : (arr[j] < pivot);
            if (condition) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        ui.showNumbersDuringSwap(arr, i, j);

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        if (i != j) {
            ui.showNumbersDuringSwap(arr, j, i);
        }
    }

}