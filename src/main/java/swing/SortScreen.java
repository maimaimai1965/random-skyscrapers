package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class SortScreen {

  private final int thresholdNumberValue;
  private final int countElementsInColumn;
  private final int sortAction;
  private final int resetAction;

  private SortScreen(
      int thresholdNumberValue, int countElementsInColumn, int sortAction, int resetAction) {
    this.thresholdNumberValue = thresholdNumberValue;
    this.countElementsInColumn = countElementsInColumn;
    this.sortAction = sortAction;
    this.resetAction = resetAction;
  }

  public static int show(
      int[] arr,
      int thresholdNumberValue,
      int countElementsInColumn,
      int sortAction,
      int resetAction) {
    AtomicInteger result = new AtomicInteger(resetAction);
    try {
      SwingUtilities.invokeAndWait(
          () ->
              result.set(
                  new SortScreen(
                          thresholdNumberValue, countElementsInColumn, sortAction, resetAction)
                      .showModal(arr)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result.get();
  }

  private int showModal(int[] arr) {
    JDialog dialog = new JDialog((JFrame) null, "Sort Screen", true);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    AtomicInteger result = new AtomicInteger(resetAction);
    dialog.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            result.set(resetAction);
            dialog.dispose();
          }
        });

    JPanel numbersPanel = createNumbersPanel(arr, dialog, result);
    JPanel actionsPanel = createActionsPanel(dialog, result);

    JPanel mainPanel = new JPanel(new BorderLayout(12, 0));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    mainPanel.add(numbersPanel, BorderLayout.CENTER);
    mainPanel.add(actionsPanel, BorderLayout.EAST);

    dialog.setContentPane(mainPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);

    return result.get();
  }

  private JPanel createNumbersPanel(int[] arr, JDialog dialog, AtomicInteger result) {
    int columns = (arr.length + countElementsInColumn - 1) / countElementsInColumn;
    JPanel panel = new JPanel(new GridLayout(countElementsInColumn, columns, 6, 6));

    for (int row = 0; row < countElementsInColumn; row++) {
      for (int col = 0; col < columns; col++) {
        int idx = col * countElementsInColumn + row;
        if (idx < arr.length) {
          JButton numberButton = new JButton(String.valueOf(arr[idx]));
          numberButton.setPreferredSize(new Dimension(64, 32));
          bindNumberAction(numberButton, arr, idx, dialog, result);
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

  private void bindNumberAction(
      JButton numberButton, int[] arr, int idx, JDialog dialog, AtomicInteger result) {
    numberButton.addActionListener(
        e -> {
          int value = arr[idx];
          if (value <= thresholdNumberValue) {
            result.set(value);
            dialog.dispose();
            return;
          }

          Integer newValue = requestReplacementValue(dialog);
          if (newValue != null) {
            arr[idx] = newValue;
            numberButton.setText(String.valueOf(newValue));
          }
        });
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

  private JPanel createActionsPanel(JDialog dialog, AtomicInteger result) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JButton sortButton = new JButton("Sort");
    JButton resetButton = new JButton("Reset");

    sortButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
    resetButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

    sortButton.addActionListener(
        e -> {
          result.set(sortAction);
          dialog.dispose();
        });

    resetButton.addActionListener(
        e -> {
          result.set(resetAction);
          dialog.dispose();
        });

    panel.add(sortButton);
    panel.add(Box.createVerticalStrut(8));
    panel.add(resetButton);
    panel.add(Box.createVerticalGlue());

    return panel;
  }
}
