/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class TextColumnInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "text";
		@NotNull ColumnInfo textColumnInfo = new TextColumnInfo(name, 20);
		@NotNull String columnName = textColumnInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				columnName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 32;
		@NotNull ColumnInfo textColumnInfo =
				new TextColumnInfo("surname", width);
		int columnWidth = textColumnInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				columnWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
		new TextColumnInfo("abyss", 0);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
		new TextColumnInfo("depth", -8);
	}

	@Test
	public void viewFormatter_ShouldReturnEmptyStringForIntegerValue() {
		@NotNull ColumnInfo textColumnInfo = new TextColumnInfo("family", 10);
		@NotNull ViewFormatter viewFormatter = textColumnInfo.getViewFormatter();
		int value = 2234714;
		@NotNull String result = viewFormatter.apply(value);
		Assert.assertEquals("viewFormatter should return empty string for integer value",
				"", result);
	}

	@Test
	public void viewFormatter_ShouldReturnSecondHalfOfStringValue() {
		@NotNull ColumnInfo textColumnInfo = new TextColumnInfo("surname", 20);
		@NotNull ViewFormatter viewFormatter = textColumnInfo.getViewFormatter();
		@NotNull String expectedResult = "watson";
		@NotNull String result = viewFormatter.apply(expectedResult + expectedResult);
		Assert.assertEquals("viewFormatter should return second half of string value",
				expectedResult, result);
	}

	@Test
	public void getJFXAlignment_ShouldReturnCenterLeft() {
		@NotNull ColumnInfo textColumnInfo = new TextColumnInfo("shift", 6);
		@NotNull Pos alignment = textColumnInfo.getJFXAlignment();
		Assert.assertEquals("getJFXAlignment() should return Pos.CENTER_LEFT",
				Pos.CENTER_LEFT, alignment);
	}
}
