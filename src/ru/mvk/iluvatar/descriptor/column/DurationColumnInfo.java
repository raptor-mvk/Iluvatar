/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

/* Descriptor for column, that contains duration measured in seconds. Duration is
represented in minutes or hours and minutes depends on its value. If duration is less,
than 1 hour, then duration is represented in minutes with a one-digit accuracy, otherwise
it is represented in hours and integer minutes.
 */
public class DurationColumnInfo extends NumColumnInfo {
  private static final char MINUTE_SUFFIX = 'м';
  private static final char HOUR_SUFFIX = 'ч';
  private static final int SECONDS_IN_HOUR = 3600;
  private static final int SECONDS_IN_MINUTE = 60;
  @NotNull
  private static final NumberFormat numberFormat =
      NumberFormat.getNumberInstance(Locale.US);

  static {
    // one-digit accuracy
    numberFormat.setMaximumFractionDigits(1);
    // no grouping for integral part digits
    numberFormat.setGroupingUsed(false);
  }

  public DurationColumnInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value instanceof Integer) {
        result = getTransformedValue((Integer) value);
      }
      return result;
    };
  }

  private String getTransformedValue(int value) {
    @NotNull StringBuilder result = new StringBuilder();
    if (value < SECONDS_IN_HOUR) {
      String normalizedValue = numberFormat.format((double) value / SECONDS_IN_MINUTE);
      result.append(normalizedValue).append(' ').append(MINUTE_SUFFIX);
    } else {
      result.append(value / SECONDS_IN_HOUR).append(' ').append(HOUR_SUFFIX);
      int minutes = value % SECONDS_IN_HOUR;
      if (minutes > 0) {
        result.append(' ').append(Math.round((double) minutes / SECONDS_IN_MINUTE))
            .append(' ').append(MINUTE_SUFFIX);
      }
    }
    return result.toString();
  }
}
