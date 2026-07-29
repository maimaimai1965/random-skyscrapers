import static org.junit.jupiter.api.Assertions.*;

import exam.QuickSortVisualizer;
import exam.SkyscrapersUi;
import org.junit.jupiter.api.Test;

class QuickSortVisualizerTest {

  private static final int THRESHOLD_VALUE = 30;
  private static final int MAX_VALUE = 1000;

  @Test
  void sortAscending() {
    QuickSortVisualizer visualizer =
        new QuickSortVisualizer(new NoOpSkyscrapersUi(), THRESHOLD_VALUE, MAX_VALUE, false);
    visualizer.generateRandomNumbers(1); // sets isDescending from initialDescending

    int[] input = {5, 1, 9, 3, 7, 2};
    int[] sorted = visualizer.sort(input.clone());

    assertArrayEquals(new int[] {1, 2, 3, 5, 7, 9}, sorted);
    assertArrayEquals(new int[] {5, 1, 9, 3, 7, 2}, input); // original unchanged
  }

  @Test
  void sortDescending() {
    QuickSortVisualizer visualizer =
        new QuickSortVisualizer(new NoOpSkyscrapersUi(), THRESHOLD_VALUE, MAX_VALUE, true);
    visualizer.generateRandomNumbers(1); // sets isDescending from initialDescending

    int[] input = {5, 1, 9, 3, 7, 2};
    int[] sorted = visualizer.sort(input.clone());

    assertArrayEquals(new int[] {9, 7, 5, 3, 2, 1}, sorted);
    assertArrayEquals(new int[] {5, 1, 9, 3, 7, 2}, input); // original unchanged
  }

  private static class NoOpSkyscrapersUi implements SkyscrapersUi {
    @Override
    public int showIntroScreen() {
      return 0;
    }

    @Override
    public void showMessage(String message) {}

    @Override
    public int showSortScreen(int[] arr) {
      return 0;
    }

    @Override
    public void clearSortScreen() {}

    @Override
    public void showNumbersDuringSwap(int[] arr, int k, int j, boolean before) {}

    @Override
    public void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement) {}

    @Override
    public int getSmallNumber() {
      return 0;
    }

    @Override
    public void setQuickSortVirtualizer(QuickSortVisualizer quickSortVisualizer) {}
  }
}
