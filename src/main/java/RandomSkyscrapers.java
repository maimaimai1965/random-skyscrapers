public class RandomSkyscrapers {

  static final int MAX_ELEMENT_COUNT = 100;
  static final int MAX_NUMBER_VALUE = 1000;
  static final int THRESHOLD_NUMBER_VALUE = 30;
  static final int COUNT_ELEMENTS_IN_COLUMN = 10;
  static final boolean INITIAL_DESCENDING = true;

  public static void main(String[] args) {

    SkyscrapersUi ui =
        new SkyscrapersUiSwingImpl(
            MAX_ELEMENT_COUNT, MAX_NUMBER_VALUE, THRESHOLD_NUMBER_VALUE, COUNT_ELEMENTS_IN_COLUMN);
    //        new SkyscrapersUiConsoleImpl(
    //            MAX_ELEMENT_COUNT, MAX_NUMBER_VALUE, THRESHOLD_NUMBER_VALUE,
    // COUNT_ELEMENTS_IN_COLUMN);
    QuickSortVisualizer visualizer =
        new QuickSortVisualizer(ui, THRESHOLD_NUMBER_VALUE, MAX_NUMBER_VALUE, INITIAL_DESCENDING);
    run(ui, visualizer);
  }

  private static void run(SkyscrapersUi ui, QuickSortVisualizer visualizer) {
    startGenerate:
    while (true) {
      int countOfElements = ui.showIntroScreen();
      int[] arr = visualizer.generateRandomNumbers(countOfElements);

      while (true) {
        int result = ui.showSortScreen(arr);
        switch (result) {
          case SkyscrapersUiConsoleImpl.SORT_ACTION:
            arr = visualizer.sort(arr);
            continue;
          case SkyscrapersUiConsoleImpl.RESET_ACTION:
            continue startGenerate;
          case SkyscrapersUiConsoleImpl.QUIT_ACTION:
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
