/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.RealDescriptor;

public class RationalColumnInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "age";
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(3, 2);
		@NotNull ColumnInfo rationalColumnInfo =
				new RationalColumnInfo(name, realDescriptor);
		@NotNull String columnName = rationalColumnInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				columnName);
	}

	@Test
	public void constructor_ShouldSetWidthAsIntegerWidthPlusFractionWidthPlus2() {
		@NotNull String name = "sum";
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, 2);
		@NotNull ColumnInfo rationalColumnInfo =
				new RationalColumnInfo(name, realDescriptor);
		int expectedWidth =
				realDescriptor.getIntegerWidth() + realDescriptor.getFractionWidth() + 2;
		int columnWidth = rationalColumnInfo.getWidth();
		Assert.assertEquals("constructor should set value of 'width' as integerWidth + " +
				"fractionWidth + 2", expectedWidth, columnWidth);
	}

	@Test
	public void viewFormatter_ShouldReturnRealValueForIntegerValue() {
		int fractionWidth = 2;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, fractionWidth);
		long multiplier = (long) Math.pow(10.0, fractionWidth);
		@NotNull String stringFormat = "%." + fractionWidth + "f";
		@NotNull ColumnInfo rationalColumnInfo =
				new RationalColumnInfo("distance", realDescriptor);
		@NotNull ViewFormatter viewFormatter = rationalColumnInfo.getViewFormatter();
		int value = 3467347;
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedValue =
				String.format(stringFormat, (double) value / multiplier);
		Assert.assertEquals("viewFormatter should return empty string for integer value",
				expectedValue, result);
	}

	@Test
	public void viewFormatter_ShouldReturnEmptyStringForStringValue() {
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(6, 3);
		@NotNull ColumnInfo rationalColumnInfo =
				new RationalColumnInfo("price", realDescriptor);
		@NotNull ViewFormatter viewFormatter = rationalColumnInfo.getViewFormatter();
		@NotNull String result = viewFormatter.apply("string");
		Assert.assertEquals("viewFormatter should return empty string for integer value",
				"", result);
	}

	@Test
	public void getJFXAlignment_ShouldReturnCenterRight() {
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, 2);
		@NotNull ColumnInfo rationalColumnInfo =
				new RationalColumnInfo("size", realDescriptor);
		@NotNull Pos alignment = rationalColumnInfo.getJFXAlignment();
		Assert.assertEquals("getJFXAlignment() should return Pos.CENTER_RIGHT",
				Pos.CENTER_RIGHT, alignment);
	}
}
