package exam;

import static exam.RandomSkyscrapers.*;

import exam.swing.IntroScreen;
import exam.swing.SortScreen;
import java.util.Scanner;

public class SkyscrapersUiSwingImpl implements SkyscrapersUi {

  public static final long SLEEP_MILLISECONDS = 300;

  private final int maxElementCount;
  private final int maxNumberValue;
  private final int thresholdNumberValue;
  private final int countElementsInColumn;
  private QuickSortVisualizer quickSortVisualizer;

  private final Scanner scanner = new Scanner(System.in);
  private SortScreen sortScreen;

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
  public void setQuickSortVirtualizer(QuickSortVisualizer quickSortVisualizer) {
    this.quickSortVisualizer = quickSortVisualizer;
  }

  @Override
  public int showIntroScreen() {
    return IntroScreen.getCount(maxElementCount, QUIT_ACTION);
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
    sortScreen =
        new SortScreen(quickSortVisualizer, arr, thresholdNumberValue, countElementsInColumn);
    int result = sortScreen.show();
    return result;
  }

  @Override
  public void clearSortScreen() {}

  @Override
  public void showNumbersDuringSwap(int[] arr, int k, int j, boolean before) {
    sortScreen.showNumbersDuringSwap(k, j, before);
  }

  @Override
  public void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement) {}

  @Override
  public int getSmallNumber() {
    while (true) {
      showMessageForGettingValue(
          "Please select a value smaller or equal to " + thresholdNumberValue + ": ");
      String result = scanner.nextLine();
      try {
        int number = Integer.parseInt(result);
        if (number > 0 && number <= thresholdNumberValue) {
          return number;
        }
        showMessage(
            "An invalid value was entered - number must be smaller or equal to "
                + thresholdNumberValue
                + "!");
      } catch (NumberFormatException e) {
        showMessage("An invalid value was entered!");
      }
    }
  }
}
