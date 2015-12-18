/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class PlainColumnInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "name";
		@NotNull ColumnInfo plainColumnInfo = new PlainColumnInfo(name, 20);
		@NotNull String columnName = plainColumnInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				columnName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 30;
		@NotNull ColumnInfo plainColumnInfo = new PlainColumnInfo("mail", width);
		int columnWidth = plainColumnInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				columnWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
		new PlainColumnInfo("nil", 0);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
		new PlainColumnInfo("any", -8);
	}

	@Test
	public void viewFormatter_ShouldCallToStringForIntegerValue() {
		@NotNull ColumnInfo plainColumnInfo = new PlainColumnInfo("width", 5);
		@NotNull ViewFormatter viewFormatter = plainColumnInfo.getViewFormatter();
		int value = 30;
		@NotNull String expectedResult = Integer.toString(value);
		@NotNull String result = viewFormatter.apply(value);
		Assert.assertEquals("viewFormatter should call toString() for integer value",
				expectedResult, result);
	}

	@Test
	public void viewFormatter_ShouldDoNothingForStringValue() {
		@NotNull ColumnInfo plainColumnInfo = new PlainColumnInfo("surname", 20);
		@NotNull ViewFormatter viewFormatter = plainColumnInfo.getViewFormatter();
		@NotNull String expectedResult = "Smith";
		@NotNull String result = viewFormatter.apply(expectedResult);
		Assert.assertEquals("viewFormatter should do nothing for string value",
				expectedResult, result);
	}

	@Test
	public void getJFXAlignment_ShouldReturnCenterRight() {
		@NotNull ColumnInfo plainColumnInfo = new PlainColumnInfo("height", 4);
		@NotNull Pos alignment = plainColumnInfo.getJFXAlignment();
		Assert.assertEquals("getJFXAlignment() should return Pos.CENTER_RIGHT",
				Pos.CENTER_RIGHT, alignment);
	}
}
