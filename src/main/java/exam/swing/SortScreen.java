package exam.swing;

import static exam.RandomSkyscrapers.*;

import exam.QuickSortVisualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicButtonUI;

public class SortScreen {

  private static SortScreen current;

  private final int thresholdNumberValue;
  private final int countElementsInColumn;

  private final exam.QuickSortVisualizer quickSortVisualizer;
  private final int[] arr;

  private JDialog dialog;
  private JButton[] numberButtons;
  private Color defaultButtonBackground;
  private Color defaultButtonForeground;

  public SortScreen(
      QuickSortVisualizer quickSortVisualizer,
      int[] arr,
      int thresholdNumberValue,
      int countElementsInColumn) {
    this.quickSortVisualizer = quickSortVisualizer;
    this.arr = arr;
    this.thresholdNumberValue = thresholdNumberValue;
    this.countElementsInColumn = countElementsInColumn;
  }

  public int show() {
    AtomicInteger result = new AtomicInteger(RESET_ACTION);

    try {
      SwingUtilities.invokeAndWait(
          () -> {
            result.set(this.showModal());
          });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result.get();
  }

  private int showModal() {
    dialog = new JDialog((JFrame) null, "Sort Screen", true);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    AtomicInteger result = new AtomicInteger(RESET_ACTION);
    dialog.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            result.set(QUIT_ACTION);
            close();
          }
        });

    numberButtons = new JButton[arr.length];
    JPanel numbersPanel = createNumbersPanel(arr, result);
    JPanel actionsPanel = createActionsPanel(result);

    JPanel mainPanel = new JPanel(new BorderLayout(12, 0));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    mainPanel.add(numbersPanel, BorderLayout.CENTER);
    mainPanel.add(actionsPanel, BorderLayout.EAST);

    dialog.setContentPane(mainPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setResizable(false);
    dialog.setVisible(true);

    return result.get();
  }

  private JPanel createNumbersPanel(int[] arr, AtomicInteger result) {
    int columns = (arr.length + countElementsInColumn - 1) / countElementsInColumn;
    JPanel panel = new JPanel(new GridLayout(countElementsInColumn, columns, 6, 6));

    for (int row = 0; row < countElementsInColumn; row++) {
      for (int col = 0; col < columns; col++) {
        int idx = col * countElementsInColumn + row;
        if (idx < arr.length) {
          JButton numberButton = new JButton(String.valueOf(arr[idx]));
          numberButton.setUI(new BasicButtonUI());
          numberButton.setOpaque(true);
          numberButton.setContentAreaFilled(true);
          numberButton.setPreferredSize(new Dimension(64, 32));
          numberButtons[idx] = numberButton;
          if (defaultButtonBackground == null) {
            defaultButtonBackground = numberButton.getBackground();
            defaultButtonForeground = numberButton.getForeground();
          }
          bindNumberAction(numberButton, arr, idx, result);
          panel.add(numberButton);
        } else {
          JPanel empty = new JPanel();
          empty.setOpaque(false);
          panel.add(empty);
        }
      }
    }

    return panel;
  }

  private void bindNumberAction(JButton numberButton, int[] arr, int idx, AtomicInteger result) {
    numberButton.addActionListener(
        e -> {
          int value = arr[idx];
          if (value <= thresholdNumberValue) {
            result.set(value);
            close();
            return;
          }

          Integer newValue = requestReplacementValue(dialog);
          if (newValue != null) {
            arr[idx] = newValue;
            numberButton.setText(String.valueOf(newValue));
          }
        });
  }

  public void showNumbersDuringSwap(int k, int j, boolean before) {
    JButton buttonK = numberButtons[k];
    if (k >= 0 && k < numberButtons.length && buttonK != null) {
      buttonK.setText(String.valueOf(arr[k]));
      highlightButton(numberButtons[k], Color.RED, Color.WHITE);
    }
    JButton buttonJ = numberButtons[j];
    if (j >= 0 && j < numberButtons.length && buttonJ != null && j != k) {
      buttonJ.setText(String.valueOf(arr[j]));
      highlightButton(buttonJ, Color.GREEN, Color.BLACK);
    }

    sleep();
    if (!before) {
      resetButtonsStyle();
    }
  }

  private void highlightButton(JButton button, Color background, Color foreground) {
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBackground(background);
    button.setForeground(foreground);
    paintButtonNow(button);
  }

  private void resetButtonsStyle() {
    Arrays.stream(numberButtons).forEach(this::resetButtonStyle);
  }

  private void resetButtonStyle(JButton button) {
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBackground(defaultButtonBackground);
    button.setForeground(defaultButtonForeground);
    paintButtonNow(button);
  }

  private void paintButtonNow(JButton button) {
    button.paintImmediately(0, 0, button.getWidth(), button.getHeight());
  }

  private void close() {
    if (dialog != null) {
      dialog.dispose();
      dialog = null;
    }
    numberButtons = null;
    if (current == this) {
      current = null;
    }
  }

  private Integer requestReplacementValue(JDialog parent) {
    while (true) {
      String text =
          JOptionPane.showInputDialog(
              parent,
              "Enter a number from 1 to " + thresholdNumberValue + ":",
              "Replace number",
              JOptionPane.QUESTION_MESSAGE);

      if (text == null) {
        return null;
      }

      try {
        int number = Integer.parseInt(text.trim());
        if (number >= 1 && number <= thresholdNumberValue) {
          return number;
        }
        JOptionPane.showMessageDialog(
            parent,
            "Number must be between 1 and " + thresholdNumberValue + "!",
            "Invalid value",
            JOptionPane.ERROR_MESSAGE);
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            parent, "An invalid value was entered!", "Invalid value", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private JPanel createActionsPanel(AtomicInteger result) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JButton sortButton = new JButton("Sort");
    JButton resetButton = new JButton("Reset");

    sortButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
    resetButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

    Dimension sortSize = sortButton.getPreferredSize();
    Dimension resetSize = resetButton.getPreferredSize();
    Dimension buttonSize =
        new Dimension(
            Math.max(sortSize.width, resetSize.width), Math.max(sortSize.height, resetSize.height));
    sortButton.setPreferredSize(buttonSize);
    sortButton.setMinimumSize(buttonSize);
    sortButton.setMaximumSize(buttonSize);
    resetButton.setPreferredSize(buttonSize);
    resetButton.setMinimumSize(buttonSize);
    resetButton.setMaximumSize(buttonSize);

    sortButton.addActionListener(
        e -> {
          result.set(SORT_ACTION);
          quickSortVisualizer.sort(arr);
        });

    resetButton.addActionListener(
        e -> {
          result.set(RESET_ACTION);
          close();
        });

    panel.add(sortButton);
    panel.add(Box.createVerticalStrut(8));
    panel.add(resetButton);
    panel.add(Box.createVerticalGlue());

    return panel;
  }

  private void sleep() {
    try {
      Thread.sleep(SkyscrapersUiSwingImpl.SLEEP_MILLISECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
