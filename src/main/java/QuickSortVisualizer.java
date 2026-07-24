import java.util.Random;

public class QuickSortVisualizer {

    private SkyscrapersUi ui;

    private boolean isDescending;
    private final int minValue;
    private final int maxValue;

    public QuickSortVisualizer(SkyscrapersUi ui, int minValue, int maxValue, boolean initialDescending) {
        this.ui = ui;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.isDescending = initialDescending;
    }

    // ==================== ГЕНЕРАЦИЯ ЧИСЕЛ ====================
    public int[] generateRandomNumbers(int count) {
        Random random = new Random();
        int[] arr = new int[count];

        // Гарантируем хотя бы одно число <= minValue
        arr[0] = random.nextInt(minValue) + 1; // от 1 до minValue

        for (int i = 1; i < count; i++) {
            arr[i] = random.nextInt(maxValue) + 1;  // от 1 до maxValue
        }

//        arr[0] = 5;
//        arr[1] = 7;
//        arr[2] = 4;
//        arr[3] = 6;

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
//        ui.showSortScreen(arrClone);
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

// TODO REMOVE
    public static void main(String[] args) throws InterruptedException {
        QuickSortVisualizer visualizer = new QuickSortVisualizer(
                new SkyscrapersUiImpl(RandomSkyscrapers.MAX_ELEMENT_COUNT, RandomSkyscrapers.MAX_NUMBER_VALUE, RandomSkyscrapers.COUNT_ELEMENTS_IN_COLUMN),
                RandomSkyscrapers.MIN_NUMBER_VALUE, RandomSkyscrapers.MAX_NUMBER_VALUE, false);

        // Генерируем случайных чисел
        int[] arr = visualizer.generateRandomNumbers(4);

        // Первая сортировка (по убыванию)
        arr = visualizer.sort(arr);

        // Вторая сортировка (по возрастанию)
        visualizer.sort(arr);

        // Можно добавить цикл для многократного нажатия
    }
}