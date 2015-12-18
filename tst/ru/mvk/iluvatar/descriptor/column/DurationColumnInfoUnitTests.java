/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.Locale;

public class DurationColumnInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "length";
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo(name, 5);
		@NotNull String columnName = durationColumnInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				columnName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 7;
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", width);
		int columnWidth = durationColumnInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				columnWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ZeroWidth_ShouldThrowIluvatarRuntimeException() {
		new DurationColumnInfo("null", 0);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NegativeWidth_ShouldThrowIluvatarRuntimeException() {
		new DurationColumnInfo("minus", -28);
	}

	@Test
	public void viewFormatter_ShouldReturnEmptyStringForLongValue() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 5);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		long value = 32867165335L;
		@NotNull String result = viewFormatter.apply(value);
		Assert.assertEquals("viewFormatter should return empty string for long value",
				"", result);
	}

	@Test
	public void viewFormatter_ShouldReturnEmptyStringForShortValue() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 5);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		short value = (short) 223;
		@NotNull String result = viewFormatter.apply(value);
		Assert.assertEquals("viewFormatter should return empty string for short value",
				"", result);
	}

	@Test
	public void viewFormatter_ShouldReturnCorrectValueForIntegerDurationLessThanHour() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 6);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		int value = 600;
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedResult = Long.toString(value / 60) + " м";
		Assert.assertEquals("viewFormatter should return correct string for integer " +
				"duration less, than 1 hour", expectedResult, result);
	}

	@Test
	public void viewFormatter_ShouldReturnCorrectValueForRealDurationLessThanHour() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 6);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		double expectedValue = 10.335;
		int value = (int) Math.floor(expectedValue * 60.0);
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedResult =
				String.format(Locale.US, "%.1f", expectedValue) + " м";
		Assert.assertEquals("viewFormatter should return correct string for real " +
				"duration less, than 1 hour", expectedResult, result);
	}

	@Test
	public void viewFormatter_ShouldReturnCorrectValueForIntegerDurationMoreThanHour() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 6);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		double expectedValue = 510;
		int value = (int) Math.floor(expectedValue * 60.0);
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedResult =
				((int) expectedValue / 60) + " ч " + ((int) expectedValue % 60) + " м";
		Assert.assertEquals("viewFormatter should return correct string for integer " +
				"duration more, than 1 hour", expectedResult, result);
	}

	@Test
	public void viewFormatter_ShouldReturnCorrectValueForRealDurationMoreThanHour() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 6);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		double expectedValue = 143.4;
		int value = (int) Math.floor(expectedValue * 60.0);
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedResult =
				((int) expectedValue / 60) + " ч " + ((int) expectedValue % 60) + " м";
		Assert.assertEquals("viewFormatter should return correct string for integer " +
				"duration more, than 1 hour", expectedResult, result);
	}

	@Test
	public void viewFormatter_ShouldReturnCorrectValueForDurationMoreThan100Hours() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("duration", 6);
		@NotNull ViewFormatter viewFormatter = durationColumnInfo.getViewFormatter();
		double expectedValue = 876650;
		int value = (int) Math.floor(expectedValue * 60.0);
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedResult =
				((int) expectedValue / 60) + " ч " + ((int) expectedValue % 60) + " м";
		Assert.assertEquals("viewFormatter should return correct string for duration more, " +
				"than 100 hours", expectedResult, result);
	}

	@Test
	public void getJFXAlignment_ShouldReturnCenterRight() {
		@NotNull ColumnInfo durationColumnInfo = new DurationColumnInfo("shift", 6);
		@NotNull Pos alignment = durationColumnInfo.getJFXAlignment();
		Assert.assertEquals("getJFXAlignment() should return Pos.CENTER_RIGHT",
				Pos.CENTER_RIGHT, alignment);
	}
}
