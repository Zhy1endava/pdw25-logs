package org.pd_week.utils;

import java.awt.GridLayout;
import java.io.File;
import javax.swing.*;

public class LogParserGUI extends JFrame {
  private final JTextField filePathField;
  private final JTextField thresholdGapField;

  public LogParserGUI() {
    setTitle("Log Parser");
    setSize(400, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(4, 3));

    filePathField = new JTextField("logs/selenium-logs.log");
    thresholdGapField = new JTextField("20");
    JButton browseButton = new JButton("Browse");
    JButton parseButton = new JButton("Parse");
    JButton exitButton = new JButton("Exit");

    add(new JLabel("Log File Path:"));
    add(filePathField);
    add(browseButton);

    add(new JLabel("Gap Threshold (ms):"));
    add(thresholdGapField);
    add(new JLabel());

    add(new JLabel());
    add(parseButton);
    add(new JLabel());

    add(new JLabel());
    add(exitButton);
    add(new JLabel());

    browseButton.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      int result = fileChooser.showOpenDialog(LogParserGUI.this);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        filePathField.setText(selectedFile.getAbsolutePath());
      }
    });

    parseButton.addActionListener(e -> {
      System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
      System.out.println("-START----------------------------------");
      String logFilePath = filePathField.getText();
      String maxGapSecondsText = thresholdGapField.getText();
      double maxGapSeconds;

      try {
        maxGapSeconds = Double.parseDouble(maxGapSecondsText);
      }
      catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(LogParserGUI.this, "Error: max_gap_seconds must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      LogParser.highlightGaps(logFilePath, maxGapSeconds);
      System.out.println("-END------------------------------------");
      System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
      System.out.println();
    });

    exitButton.addActionListener(e -> System.exit(0));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LogParserGUI().setVisible(true));
  }
}
