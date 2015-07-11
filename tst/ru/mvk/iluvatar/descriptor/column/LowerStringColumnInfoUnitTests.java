/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class LowerStringColumnInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "text";
    @NotNull ColumnInfo lowerStringColumnInfo = new StringColumnInfo(name, 20);
    @NotNull String columnName = lowerStringColumnInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
                           columnName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 32;
    @NotNull ColumnInfo lowerStringColumnInfo =
        new StringColumnInfo("surname", width);
    int columnWidth = lowerStringColumnInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
                           columnWidth);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
    new StringColumnInfo("abyss", 0);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
    new StringColumnInfo("depth", -8);
  }

  @Test
  public void viewFormatter_ShouldReturnEmptyStringForIntegerValue() {
    @NotNull ColumnInfo lowerStringColumnInfo = new StringColumnInfo("family", 10);
    @NotNull ViewFormatter viewFormatter = lowerStringColumnInfo.getViewFormatter();
    int value = 2234714;
    @NotNull String result = viewFormatter.apply(value);
    Assert.assertEquals("viewFormatter should return empty string for integer value",
                           "", result);
  }

  @Test
  public void viewFormatter_ShouldReturnSecondHalfOfStringValue() {
    @NotNull ColumnInfo lowerStringColumnInfo = new StringColumnInfo("surname", 20);
    @NotNull ViewFormatter viewFormatter = lowerStringColumnInfo.getViewFormatter();
    @NotNull String expectedResult = "watson";
    @NotNull String result = viewFormatter.apply(expectedResult + expectedResult);
    Assert.assertEquals("viewFormatter should return second half of string value",
                           expectedResult, result);
  }
}
