package exam;

import java.util.Arrays;
import java.util.Random;

public class QuickSortVisualizer {

  private final SkyscrapersUi ui;

  private final boolean initialDescending;
  private boolean isDescending;
  private final int thresholdValue;
  private final int maxValue;

  private final Random random = new Random();

  public QuickSortVisualizer(
      SkyscrapersUi ui, int thresholdValue, int maxValue, boolean initialDescending) {
    this.ui = ui;
    this.thresholdValue = thresholdValue;
    this.maxValue = maxValue;
    this.initialDescending = initialDescending;
    ui.setQuickSortVirtualizer(this);
  }

  // ==================== ГЕНЕРАЦИЯ ЧИСЕЛ ====================
  public int[] generateRandomNumbers(int count) {
    isDescending = initialDescending;
    int[] arr = new int[count];

    // Гарантируем хотя бы одно число <= minValue
    arr[0] = random.nextInt(thresholdValue) + 1; // от 1 до minValue

    for (int i = 1; i < count; i++) {
      arr[i] = random.nextInt(maxValue) + 1; // от 1 до maxValue
    }
    return arr;
  }

  // ==================== ОСНОВНАЯ СОРТИРОВКА ====================
  public int[] sort(int[] arr) {
    if (arr == null || arr.length == 0) {
      return new int[0];
    }

    String direction = isDescending ? "ПО УБЫВАНИЮ" : "ПО ВОЗРАСТАНИЮ";
    ui.showMessage("=== Запуск QuickSort " + direction + " ===");
    quickSort(arr, 0, arr.length - 1);

    // Переключаем направление
    isDescending = !isDescending;

    ui.clearSortScreen();
    return arr;
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
    System.out.print("swap before " + i + " (" + arr[i] + ") <-> " + j + " (" + arr[j] + ") : ");
    System.out.println(Arrays.toString(arr));
    ui.showNumbersDuringSwap(arr, i, j, true);

    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
    System.out.print("swap after " + i + " (" + arr[i] + ") <-> " + j + " (" + arr[j] + ") : ");
    System.out.println(Arrays.toString(arr));
    ui.showNumbersDuringSwap(arr, j, i, false);
  }
}
