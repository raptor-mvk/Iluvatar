/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.Locale;

public class FileSizeColumnInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "size";
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo(name, 20);
    @NotNull String columnName = fileSizeColumnInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        columnName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 10;
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("file", width);
    int columnWidth = fileSizeColumnInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        columnWidth);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
    new FileSizeColumnInfo("empty", 0);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
    new FileSizeColumnInfo("less", -57);
  }

  @Test
  public void viewFormatter_ShouldReturnEmptyStringForIntegerValue() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    int value = 2234714;
    @NotNull String result = viewFormatter.apply(value);
    Assert.assertEquals("viewFormatter should return empty string for integer value",
        "", result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeLessThanKb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    long value = 1018L;
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult = Long.toString(value) + " б";
    Assert.assertEquals("viewFormatter should return correct string for size less, " +
        "than 1 Kb", expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeLessThanMb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    double expectedValue = 817.02;
    long value = (long) Math.floor(expectedValue * 1024.0);
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult =
        String.format(Locale.US, "%.2f", expectedValue) + " Кб";
    Assert.assertEquals("viewFormatter should return correct string for size less, " +
        "than 1 Kb", expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeLessThanGb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    double expectedValue = 1.2;
    long value = (long) Math.floor(expectedValue * 1048576.0);
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult =
        String.format(Locale.US, "%.1f", expectedValue) + " Мб";
    Assert.assertEquals("viewFormatter should return correct string for size less, " +
        "than 1 Gb", expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeLessThanTb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    double expectedValue = 342.78;
    long value = (long) Math.floor(expectedValue * 1073741824.0);
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult =
        String.format(Locale.US, "%.2f", expectedValue) + " Гб";
    Assert.assertEquals("viewFormatter should return correct string for size less, " +
        "than 1 Tb", expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeMoreThanTb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    double expectedValue = 2.0;
    long value = (long) Math.floor(expectedValue * 1099511627776.0);
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult =
        String.format(Locale.US, "%.0f", expectedValue) + " Тб";
    Assert.assertEquals("viewFormatter should return correct string for size more, " +
        "than 1 Tb", expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldReturnCorrectValueForSizeMoreThan1024Tb() {
    @NotNull ColumnInfo fileSizeColumnInfo = new FileSizeColumnInfo("size", 10);
    @NotNull ViewFormatter viewFormatter = fileSizeColumnInfo.getViewFormatter();
    double expectedValue = 2047.15;
    long value = (long) Math.floor(expectedValue * 1099511627776.0);
    @NotNull String result = viewFormatter.apply(value);
    @NotNull String expectedResult =
        String.format(Locale.US, "%.2f", expectedValue) + " Тб";
    Assert.assertEquals("viewFormatter should return correct string for size more, " +
        "than 1024 Tb", expectedResult, result);
  }
}
