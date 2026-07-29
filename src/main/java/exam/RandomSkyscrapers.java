package exam;

import exam.console.SkyscrapersUiConsoleImpl;
import exam.swing.SkyscrapersUiSwingImpl;

public class RandomSkyscrapers {

  public static final int SORT_ACTION = -1;
  public static final int RESET_ACTION = -2;
  public static final int QUIT_ACTION = -3;

  public static final int MAX_ELEMENT_COUNT = 100;
  public static final int MAX_NUMBER_VALUE = 1000;
  public static final int THRESHOLD_NUMBER_VALUE = 30;
  public static final int COUNT_ELEMENTS_IN_COLUMN = 10;
  public static final boolean INITIAL_DESCENDING = true;

  public static void main(String[] args) {
    boolean useConsole = false;
    for (String arg : args) {
      if ("console".equals(arg)) {
        useConsole = true;
        break;
      }
    }

    SkyscrapersUi ui =
        useConsole
            ? new SkyscrapersUiConsoleImpl(
                MAX_ELEMENT_COUNT,
                MAX_NUMBER_VALUE,
                THRESHOLD_NUMBER_VALUE,
                COUNT_ELEMENTS_IN_COLUMN)
            : new SkyscrapersUiSwingImpl(
                MAX_ELEMENT_COUNT,
                MAX_NUMBER_VALUE,
                THRESHOLD_NUMBER_VALUE,
                COUNT_ELEMENTS_IN_COLUMN);
    QuickSortVisualizer visualizer =
        new QuickSortVisualizer(ui, THRESHOLD_NUMBER_VALUE, MAX_NUMBER_VALUE, INITIAL_DESCENDING);
    run(ui, visualizer);
  }

  private static void run(SkyscrapersUi ui, QuickSortVisualizer visualizer) {
    startGenerate:
    while (true) {
      int countOfElements = ui.showIntroScreen();
      if (countOfElements == QUIT_ACTION) {
        System.exit(0);
      }
      int[] arr = visualizer.generateRandomNumbers(countOfElements);

      while (true) {
        int result = ui.showSortScreen(arr);
        switch (result) {
          case SORT_ACTION:
            visualizer.sort(arr);
            continue;
          case RESET_ACTION:
            continue startGenerate;
          case QUIT_ACTION:
            System.exit(0);
          default:
            if (result <= THRESHOLD_NUMBER_VALUE) {
              arr = visualizer.generateRandomNumbers(result);
            } else {
              ui.replaceElementByNew(arr, result, ui.getSmallNumber());
            }
        }
      }
    }
  }
}
