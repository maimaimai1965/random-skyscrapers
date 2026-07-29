package exam;

import static exam.RandomSkyscrapers.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SkyscrapersUiConsoleImpl implements SkyscrapersUi {

  private static final int SLEEP_MILLISECONDS = 300;

  private final int maxElementCount;
  private final int maxNumberValue;
  private final int thresholdNumberValue;
  private final int countElementsInColumn;

  private final Scanner scanner = new Scanner(System.in);

  SkyscrapersUiConsoleImpl(
      int maxElementCount,
      int maxNumberValue,
      int thresholdNumberValue,
      int countElementsInColumn) {
    this.maxElementCount = maxElementCount;
    this.maxNumberValue = maxNumberValue;
    this.thresholdNumberValue = thresholdNumberValue;
    this.countElementsInColumn = countElementsInColumn;
  }

  @Override
  public int showIntroScreen() {
    int count;
    while (true) {
      try {
        showMessageForGettingValue("How many numbers to display?: ");
        String line = scanner.nextLine();
        count = Integer.parseInt(line);
        if (count > maxElementCount || count <= 0) {
          showMessage(String.format("Number must be between 1 and %d!", maxElementCount));
        } else {
          break;
        }
      } catch (Exception e) {
        showMessage("An invalid value was entered!");
      }
    }
    return count;
  }

  private int getDataFromSortScreen(int[] arr) {
    String newLine = System.lineSeparator();
    while (true) {
      showMessageForGettingValue(
          newLine + "Enter (S)ort or (R)eset or Value from the list or (Q)uit: ");
      newLine = "";
      String result = scanner.nextLine();
      if (result.equalsIgnoreCase("S")) {
        return SORT_ACTION;
      } else if (result.equalsIgnoreCase("R")) {
        return RESET_ACTION;
      } else if (result.equalsIgnoreCase("Q")) {
        return QUIT_ACTION;
      } else {
        try {
          int number = Integer.parseInt(result);
          if (number > 0 && number <= maxNumberValue) {
            for (int n : arr) {
              if (n == number) {
                return number;
              }
            }
          }
          showMessage("An invalid value was entered - number must be present in the list!");
        } catch (NumberFormatException e) {
          showMessage("An invalid value was entered!");
        }
      }
    }
  }

  @Override
  public void showMessage(String message) {
    System.out.println(message);
  }

  public void showMessageForGettingValue(String message) {
    System.out.print(message);
  }

  @Override
  public int showSortScreen(int[] arr) {
    showNumbers(arr);
    return getDataFromSortScreen(arr);
  }

  public void clearSortScreen() {
    clearLine();
  }

  private void showNumbers(int[] arr) {
    int groupCount = (arr.length + countElementsInColumn - 1) / countElementsInColumn;
    for (int g = 0; g < groupCount; g++) {
      if (g > 0) {
        System.out.print(" ");
      }
      System.out.print("[ ");
      for (int i = 0; i < countElementsInColumn; i++) {
        if (i > 0) {
          System.out.print(", ");
        }
        int idx = g * countElementsInColumn + i;
        if (idx < arr.length) {
          System.out.print(arr[idx]);
        }
      }
      System.out.print(" ]");
    }
  }

  @Override
  public void showNumbersDuringSwap(int[] arr, int k, int j, boolean before) {
    if (!before && k == j) {
      return;
    }
    clearLine();
    int groupCount = (arr.length + countElementsInColumn - 1) / countElementsInColumn;
    for (int g = 0; g < groupCount; g++) {
      if (g > 0) {
        System.out.print(" ");
      }
      System.out.print("[ ");
      for (int i = 0; i < countElementsInColumn; i++) {
        if (i > 0) {
          System.out.print(", ");
        }
        int idx = g * countElementsInColumn + i;
        if (idx < arr.length) {
          if (idx == k) {
            System.out.print("\u001B[91m\u001B[5m" + arr[idx] + "\u001B[0m"); // красный
          } else if (idx == j) {
            System.out.print("\u001B[32m\u001B[5m" + arr[idx] + "\u001B[0m"); // зелёный
          } else {
            System.out.print(arr[idx]);
          }
        }
      }
      System.out.print(" ]");
    }
    sleep();
  }

  public void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == valueOfElement) {
        arr[i] = newValueOfElement;
        return; // Меняем только первый совпавший элемент.
      }
    }
  }

  public int getSmallNumber() {
    while (true) {
      showMessageForGettingValue(
          "Please select a value smaller or equal to " + THRESHOLD_NUMBER_VALUE + ": ");
      String result = scanner.nextLine();
      try {
        int number = Integer.parseInt(result);
        if (number > 0 && number <= thresholdNumberValue) {
          return number;
        }
        showMessage(
            "An invalid value was entered - number must be smaller or equal to "
                + THRESHOLD_NUMBER_VALUE
                + "!");
      } catch (NumberFormatException e) {
        showMessage("An invalid value was entered!");
      }
    }
  }

  private void clearLine() {
    System.out.print("\r");
    System.out.print(" ".repeat(100)); // Очищаем строку пробелами
    System.out.print("\r");
  }

  private void sleep() {
    try {
      TimeUnit.MILLISECONDS.sleep(SLEEP_MILLISECONDS); // Задержка для удобства просмотра
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setQuickSortVirtualizer(QuickSortVisualizer quickSortVisualizer) {}
}
