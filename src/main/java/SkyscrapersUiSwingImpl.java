import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import swing.IntroScreen;
import swing.SortScreen;

public class SkyscrapersUiSwingImpl implements SkyscrapersUi {

  private static final int SLEEP_MILLISECONDS = 300;

  public static final int SORT_ACTION = -1;
  public static final int RESET_ACTION = -2;
  public static final int QUIT_ACTION = -3;

  private final int maxElementCount;
  private final int maxNumberValue;
  private final int thresholdNumberValue;
  private final int countElementsInColumn;

  private final Scanner scanner = new Scanner(System.in);

  SkyscrapersUiSwingImpl(
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
    // Use Swing window instead of console input.
    return IntroScreen.getCount(maxElementCount);
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
    return SortScreen.show(
        arr, thresholdNumberValue, countElementsInColumn, SORT_ACTION, RESET_ACTION);
  }

  @Override
  public void clearSortScreen() {
    clearLine();
  }

  @Override
  public void showNumbersDuringSwap(int[] arr, int k, int j) {
    // k - красный
    // j - зелёный
    sleep();
  }

  @Override
  public void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == valueOfElement) {
        arr[i] = newValueOfElement;
        return; // Меняем только первый совпавший элемент.
      }
    }
  }

  @Override
  public int getSmallNumber() {
    while (true) {
      showMessageForGettingValue("Please select a value smaller or equal to 30: ");
      String result = scanner.nextLine();
      try {
        int number = Integer.parseInt(result);
        if (number > 0 && number <= thresholdNumberValue) {
          return number;
        }
        showMessage("An invalid value was entered - number must be smaller or equal to 30!");
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
}
