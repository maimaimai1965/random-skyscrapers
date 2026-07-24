import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SkyscrapersUiImpl implements SkyscrapersUi {

    private int SLEEP_MILLISECONDS = 700;

    public static final int SORT_ACTION = -1;
    public static final int RESET_ACTION = -2;
    public static final int QUIT_ACTION = -3;

    private final int maxElementCount;
    private final int maxNumberValue;
    private final int countElementsInColumn;

    private Scanner scanner = new Scanner(System.in);

    SkyscrapersUiImpl(int maxElementCount, int maxNumberValue, int countElementsInColumn) {
        this.maxElementCount = maxElementCount;
        this.maxNumberValue = maxNumberValue;
        this.countElementsInColumn = countElementsInColumn;
    }

    @Override
    public int showIntroScreen() {
        int count = 0;
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
            showMessageForGettingValue(newLine + "Enter (S)ort or (R)eset or Value from the list or (Q)uit: ");
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
                        for (int n: arr) {
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
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(arr[i]);
        }
        System.out.print(" ]");
    }

    private void showNumbersInOnColumn(int[] arr) {
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(arr[i]);
        }
        System.out.print(" ]");
    }


    @Override
    public void showNumbersDuringSwap(int[] arr, int k, int j) {
        clearLine();
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0)
                System.out.print(", ");

            // Подсветка (симуляция)
            if (i == k) {
                System.out.print("\u001B[91m\u001B[5m" + arr[i] + "\u001B[0m"); // красный
            } else {
                if (i == j) {
                    System.out.print("\u001B[32m\u001B[5m" + arr[i] + "\u001B[0m"); // зелёный
                } else {
                    System.out.print(arr[i]);
                }
            }
        }
        System.out.print(" ]");
        sleep();
    }

    public void showNumbersDuringSwapInOneLine(int[] arr, int k, int j) {
        clearLine();
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0)
                System.out.print(", ");

            // Подсветка (симуляция)
            if (i == k) {
                System.out.print("\u001B[91m\u001B[5m" + arr[i] + "\u001B[0m"); // красный
            } else {
                if (i == j) {
                    System.out.print("\u001B[32m\u001B[5m" + arr[i] + "\u001B[0m"); // зелёный
                } else {
                    System.out.print(arr[i]);
                }
            }
        }
        System.out.print(" ]");
        sleep();
    }

    public void replaceElementByNew(int[] arr, int valueOfElement, int newValueOfElement) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == valueOfElement) {
                arr[i] = newValueOfElement;
                return;  // Меняем только первый совпавший элемент.
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
