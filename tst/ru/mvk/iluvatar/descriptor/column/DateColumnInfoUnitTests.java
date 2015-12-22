/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.TemporalDescriptor;

import java.time.LocalDate;

public class DateColumnInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "birthday";
		@NotNull LocalDate defaultValue = LocalDate.of(2010, 1, 5);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd.MM.yy");
		@NotNull ColumnInfo dateColumnInfo =
				new DateColumnInfo(name, temporalDescriptor);
		@NotNull String columnName = dateColumnInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				columnName);
	}

	@Test
	public void constructor_ShouldSetWidthAsPatternLength() {
		@NotNull String name = "enrollmentDate";
		@NotNull LocalDate defaultValue = LocalDate.of(2000, 4, 8);
		@NotNull String pattern = "yyyy/MM/dd";
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, pattern);
		@NotNull ColumnInfo dateColumnInfo =
				new DateColumnInfo(name, temporalDescriptor);
		int expectedWidth = temporalDescriptor.getWidth();
		int columnWidth = dateColumnInfo.getWidth();
		Assert.assertEquals("constructor should set value of 'width' as pattern length",
				expectedWidth, columnWidth);
	}

	@Test
	public void viewFormatter_ShouldReturnFormattedForLocalDateValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(2004, 8, 12);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd.MM.yy");
		@NotNull ColumnInfo dateColumnInfo =
				new DateColumnInfo("supplyDate", temporalDescriptor);
		@NotNull ViewFormatter viewFormatter = dateColumnInfo.getViewFormatter();
		@NotNull LocalDate value = LocalDate.of(2011, 3, 9);
		@NotNull String result = viewFormatter.apply(value);
		@NotNull String expectedValue = value.format(temporalDescriptor.getFormatter());
		Assert.assertEquals("viewFormatter should return formatted date for LocalDate value",
				expectedValue, result);
	}

	@Test
	public void viewFormatter_ShouldReturnDefaultDateForStringValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(2012, 12, 31);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd.MM.yy");
		@NotNull ColumnInfo dateColumnInfo =
				new DateColumnInfo("finishDate", temporalDescriptor);
		@NotNull ViewFormatter viewFormatter = dateColumnInfo.getViewFormatter();
		@NotNull String result = viewFormatter.apply("string");
		@NotNull String expectedResult =
				defaultValue.format(temporalDescriptor.getFormatter());
		Assert.assertEquals("viewFormatter should return default date for string value",
				expectedResult, result);
	}

	@Test
	public void getJFXAlignment_ShouldReturnCenterLeft() {
		@NotNull LocalDate defaultValue = LocalDate.of(2005, 3, 19);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd.MM.yy");
		@NotNull ColumnInfo dateColumnInfo =
				new DateColumnInfo("startDate", temporalDescriptor);
		@NotNull Pos alignment = dateColumnInfo.getJFXAlignment();
		Assert.assertEquals("getJFXAlignment() should return Pos.CENTER_LEFT",
				Pos.CENTER_LEFT, alignment);
	}
}
