/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "graduation";
		@NotNull LocalDate defaultValue = LocalDate.of(2007, 3, 2);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd.MM.yyyy");
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo(name, temporalDescriptor);
		@NotNull String dateFieldName = dateFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				dateFieldName);
	}

	@Test
	public void constructor_ShouldSetWidthAsPatternLength() {
		@NotNull LocalDate defaultValue = LocalDate.of(2010, 5, 15);
		@NotNull String pattern = "yy/MM/dd";
		int width = pattern.length();
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, pattern);
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo("birthday", temporalDescriptor);
		int dateFieldWidth = dateFieldInfo.getWidth();
		Assert.assertEquals("constructor should set value of 'width' as pattern length",
				width, dateFieldWidth);
	}

	@Test
	public void constructor_ShouldSetDefaultValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(1998, 11, 14);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd-MM-yyyy");
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo("date", temporalDescriptor);
		@NotNull LocalDate dateFieldDefaultValue = dateFieldInfo.getDefaultValue();
		Assert.assertEquals("constructor should set correct value of 'defaultValue'",
				defaultValue, dateFieldDefaultValue);
	}

	@Test
	public void constructor_ShouldSetFormatter() {
		@NotNull LocalDate defaultValue = LocalDate.of(1999, 8, 12);
		@NotNull String pattern = "yy.MM.dd";
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, pattern);
		@NotNull String expectedValue = defaultValue.format(formatter);
		@NotNull DateTimeFormatter dateFieldFormatter = temporalDescriptor.getFormatter();
		@NotNull String value = defaultValue.format(dateFieldFormatter);
		Assert.assertEquals("constructor should set correct value of 'formatter'",
				expectedValue, value);
	}

	@Test
	public void getJFXFieldClassName_ShouldReturnDateField() {
		@NotNull LocalDate defaultValue = LocalDate.of(2001, 7, 31);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "yy-MM-dd");
		@NotNull TemporalFieldInfo dateFieldInfo =
				new DateFieldInfo("active", temporalDescriptor);
		@NotNull String fieldName = dateFieldInfo.getJFXFieldClassName();
		Assert.assertEquals("getJFXFieldClassName() should return DateField",
				"ru.mvk.iluvatar.javafx.field.DateField", fieldName);
	}
}
