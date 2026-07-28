package exam.swing;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class IntroScreen {

  private final int maxElementCount;

  private IntroScreen(int maxElementCount) {
    this.maxElementCount = maxElementCount;
  }

  public static int getCount(int maxElementCount) {
    AtomicInteger result = new AtomicInteger(Integer.MIN_VALUE);
    try {
      SwingUtilities.invokeAndWait(
          () -> {
            result.set(new IntroScreen(maxElementCount).showModal());
          });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result.get();
  }

  private int showModal() {
    JDialog dialog = new JDialog((Frame) null, "Random Skyscrapers", true);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    AtomicInteger result = new AtomicInteger(Integer.MIN_VALUE);

    JLabel label = new JLabel("How many numbers to display:");
    JTextField field = new JTextField(10);
    JButton enterButton = new JButton("Enter");

    JLabel errorLabel = new JLabel(" ");
    errorLabel.setForeground(Color.RED);

    Runnable submit =
        () -> {
          String text = field.getText() == null ? "" : field.getText().trim();
          try {
            int count = Integer.parseInt(text);
            if (count <= 0 || count > maxElementCount) {
              errorLabel.setText("Number must be between 1 and " + maxElementCount + "!");
              return;
            }
            result.set(count);
            dialog.dispose();
          } catch (NumberFormatException ex) {
            errorLabel.setText("An invalid value was entered!");
          }
        };

    enterButton.addActionListener(e -> submit.run());
    field.addActionListener(e -> submit.run()); // handle Enter key
    dialog.getRootPane().setDefaultButton(enterButton);

    JPanel inputPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 4, 8);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    inputPanel.add(label, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(8, 0, 4, 8);
    inputPanel.add(field, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(4, 0, 4, 8);
    inputPanel.add(enterButton, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(4, 8, 4, 8);
    inputPanel.add(errorLabel, gbc);

    dialog.getContentPane().setLayout(new BorderLayout());
    dialog.getContentPane().add(inputPanel, BorderLayout.CENTER);

    // Keep the UI visible and focused
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true); // blocks until disposed

    return result.get(); // Should always be set by pressing "Enter"
  }
}
