/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class StringColumnInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "name";
    @NotNull ColumnInfo stringColumnInfo = new NumColumnInfo(name, 20);
    @NotNull String columnName = stringColumnInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        columnName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 30;
    @NotNull ColumnInfo stringColumnInfo = new NumColumnInfo("mail", width);
    int columnWidth = stringColumnInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        columnWidth);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
    new NumColumnInfo("nil", 0);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
    new NumColumnInfo("any", -8);
  }

  @Test
  public void viewFormatter_ShouldCallToStringForIntegerValue() {
    @NotNull ColumnInfo stringColumnInfo = new NumColumnInfo("width", 5);
    @NotNull ViewFormatter viewFormatter = stringColumnInfo.getViewFormatter();
    int value = 30;
    @NotNull String expectedResult = Integer.toString(value);
    @NotNull String result = viewFormatter.apply(value);
    Assert.assertEquals("viewFormatter should call toString() for integer value",
        expectedResult, result);
  }

  @Test
  public void viewFormatter_ShouldDoNothingForStringValue() {
    @NotNull ColumnInfo stringColumnInfo = new NumColumnInfo("surname", 20);
    @NotNull ViewFormatter viewFormatter = stringColumnInfo.getViewFormatter();
    @NotNull String expectedResult = "Smith";
    @NotNull String result = viewFormatter.apply(expectedResult);
    Assert.assertEquals("viewFormatter should do nothing for string value",
        expectedResult, result);
  }
}
