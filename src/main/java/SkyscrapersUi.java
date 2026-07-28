public interface SkyscrapersUi {

    int showIntroScreen();
    void showMessage(String message);
    int showSortScreen(int[] arr);
    void clearSortScreen();
    void showNumbersDuringSwap(int[] arr, int k, int j);
    void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement);
    int getSmallNumber();

}
