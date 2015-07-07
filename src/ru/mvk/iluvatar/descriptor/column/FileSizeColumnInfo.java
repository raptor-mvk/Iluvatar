/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

/* Descriptor for column, that contains size of a file measured in bytes. Size is
represented in bytes, Kilobytes, Megabytes, Gigabytes or Terabytes depends on integral
part of representation of size in this units. If integral part is greater than 0 and less
than 1024, then corresponding unit is selected. If size is greater than 1024 Tb, then Tb
is selected. Size is represented with a two-digit accuracy, except for bytes.
*/
public class FileSizeColumnInfo extends NumColumnInfo {
  private static final char UNIT = 'б';
  @NotNull
  private static final char UNIT_PREFIXES[] = {'К', 'М', 'Г', 'Т', 'Т'};
  // shift, that used instead of dividing by coefficient
  private static final int FOUNDATION_SHIFT = 10;
  // coefficient of transformation between units
  private static final long FOUNDATION = 1024L;
  @NotNull
  private static final NumberFormat numberFormat =
      NumberFormat.getNumberInstance(Locale.US);

  static {
    // two-digit accuracy
    numberFormat.setMaximumFractionDigits(2);
    // no grouping for integral part digits
    numberFormat.setGroupingUsed(false);
  }

  public FileSizeColumnInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value instanceof Long) {
        result = getNormalizedValue((Long) value);
      }
      return result;
    };
  }

  private String getNormalizedValue(long value) {
    @NotNull StringBuilder result = new StringBuilder();
    int unitIndex = -1;
    int shift = 0;
    while (value >> shift > FOUNDATION && ++unitIndex < UNIT_PREFIXES.length - 1) {
      shift += FOUNDATION_SHIFT;
    }
    // value & ((1L << shift) - 1)) equals remainder of division by coefficient of
    // transformation current unit into bytes
    if (unitIndex == -1 || (value & ((1L << shift) - 1)) == 0L) {
      result.append(value >> shift);
    } else {
      @NotNull
      String normalizedValue = numberFormat.format((double) value / (1L << shift));
      result.append(normalizedValue);
    }
    result.append(' ');
    if (unitIndex > -1) {
      result.append(UNIT_PREFIXES[unitIndex]);
    }
    result.append(UNIT);
    return result.toString();
  }
}
