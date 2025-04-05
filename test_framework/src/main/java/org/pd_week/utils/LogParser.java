package org.pd_week.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

  private static final Pattern TIMESTAMP_PATTERN_WITH_DATE = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s[A-Z]{3,4}).*");
  private static final Pattern TIMESTAMP_PATTERN_NO_DATE = Pattern.compile("^(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}).*");

  private static final DateTimeFormatter FORMATTER_WITH_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");
  private static final DateTimeFormatter FORMATTER_NO_DATE = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

  public static class GapInfo {
    public final int previousLineNumber;
    public final String previousLine;
    public final String currentLine;
    public final double gapMilliseconds;

    public GapInfo(int previousLineNumber, String previousLine, String currentLine, double gapMilliseconds) {
      this.previousLineNumber = previousLineNumber;
      this.previousLine = previousLine;
      this.currentLine = currentLine;
      this.gapMilliseconds = gapMilliseconds;
    }

    @Override
    public String toString() {
      return String.format("Line %d:%n  Previous: %s%n  Current:  %s%n  Gap:      %.3f milliseconds%n",
                           previousLineNumber, previousLine, currentLine, gapMilliseconds);
    }
  }

  public static List<GapInfo> findTimestampGaps(String logFilePath, double thresholdGapMilliseconds, List<Double> executionTime) {
    List<GapInfo> gaps = new ArrayList<>();
    ZonedDateTime previousTimestamp = null, firstTimestamp = null, lastTimestamp = null;
    String previousLine = null;
    int lineNumber = 0;

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(logFilePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineNumber++;
        ZonedDateTime currentTimestamp = null;
        Matcher matcherWithDate = TIMESTAMP_PATTERN_WITH_DATE.matcher(line);
        Matcher matcherNoDate = TIMESTAMP_PATTERN_NO_DATE.matcher(line);

        if (matcherWithDate.matches()) {
          try {
            currentTimestamp = ZonedDateTime.parse(matcherWithDate.group(1), FORMATTER_WITH_DATE);
          } catch (DateTimeParseException e) {
            System.err.printf("Warning: Invalid timestamp format on line %d: %s%n", lineNumber, line);
            continue;
          }
        } else if (matcherNoDate.matches()) {
          try {
            LocalTime localTime = LocalTime.parse(matcherNoDate.group(1), FORMATTER_NO_DATE);
            currentTimestamp = localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault());
          } catch (DateTimeParseException e) {
            System.err.printf("Warning: Invalid timestamp format on line %d: %s%n", lineNumber, line);
            continue;
          }
        }

        if (currentTimestamp != null) {
          if (firstTimestamp == null) firstTimestamp = currentTimestamp;
          lastTimestamp = currentTimestamp;

          if (previousTimestamp != null) {
            double diffMilliseconds = Duration.between(previousTimestamp, currentTimestamp).toMillis();
            if (diffMilliseconds > thresholdGapMilliseconds) {
              gaps.add(new GapInfo(lineNumber - 1, previousLine, line, diffMilliseconds));
            }
          }

          previousTimestamp = currentTimestamp;
          previousLine = line;
        }
      }
    } catch (IOException e) {
      System.err.printf("An error occurred reading '%s': %s%n", logFilePath, e.getMessage());
    }

    if (firstTimestamp != null) {
      double totalMilliseconds = Duration.between(firstTimestamp, lastTimestamp).toMillis();
      executionTime.add(totalMilliseconds);
    }

    return gaps;
  }

  public static void highlightGaps(String logFilePath, double thresholdGapMilliseconds) {
    List<Double> executionTime = new ArrayList<>();
    List<GapInfo> gaps = findTimestampGaps(logFilePath, thresholdGapMilliseconds, executionTime);

    if (!gaps.isEmpty()) {
      double totalGapMilliseconds = gaps.stream().mapToDouble(gap -> gap.gapMilliseconds).sum();

      System.out.printf("Timestamp gaps greater than %.3f milliseconds found in '%s':%n", thresholdGapMilliseconds, logFilePath);
      System.out.println("----------------------------------------");
      gaps.forEach(gap -> {
        System.out.println(gap);
        System.out.println("----------------------------------------");
      });

      System.out.printf("Total potential time to save: %.3f milliseconds (%.2f seconds; %.2f minutes)%n", totalGapMilliseconds, totalGapMilliseconds / 1000, totalGapMilliseconds / 1000 / 60);
    } else {
      System.out.printf("No timestamp gaps greater than %.3f milliseconds found in '%s'.%n", thresholdGapMilliseconds, logFilePath);
    }

    if (!executionTime.isEmpty()) {
      double totalTime = executionTime.get(0);
      System.out.printf("Total execution time: %.3f milliseconds (%.2f seconds; %.2f minutes)%n", totalTime, totalTime / 1000, totalTime / 1000 / 60);
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java LogParser <log_file_path> <threshold_gap_milliseconds>");
      return;
    }

    String logFilePath = args[0];
    double thresholdGapMilliseconds;

    try {
      thresholdGapMilliseconds = Double.parseDouble(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Error: threshold_gap_milliseconds must be a valid number.");
      return;
    }

    highlightGaps(logFilePath, thresholdGapMilliseconds);
  }
}
