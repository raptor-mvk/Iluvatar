/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "graduation";
		@NotNull LocalDate defaultValue = LocalDate.of(2007, 3, 2);
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo(name, 7, temporalDescriptor);
		@NotNull String dateFieldName = dateFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				dateFieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 10;
		@NotNull LocalDate defaultValue = LocalDate.of(2010, 5, 15);
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo("birthday", width, temporalDescriptor);
		int dateFieldWidth = dateFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				dateFieldWidth);
	}

	@Test
	public void constructor_ShouldSetDefaultValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(1998, 11, 14);
		@NotNull DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern("dd-MM-yyyy");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo("date", 8, temporalDescriptor);
		@NotNull LocalDate dateFieldDefaultValue = dateFieldInfo.getDefaultValue();
		Assert.assertEquals("constructor should set correct value of 'defaultValue'",
				defaultValue, dateFieldDefaultValue);
	}

	@Test
	public void constructor_ShouldSetFormatter() {
		@NotNull LocalDate defaultValue = LocalDate.of(1999, 8, 12);
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull TemporalFieldInfo<LocalDate> dateFieldInfo =
				new DateFieldInfo("enrollment", 6, temporalDescriptor);
		@NotNull DateTimeFormatter dateFieldFormatter = dateFieldInfo.getFormatter();
		Assert.assertEquals("constructor should set correct value of 'formatter'", formatter,
				dateFieldFormatter);
	}

	@Test
	public void getJFXFieldClassName_ShouldReturnDateField() {
		@NotNull LocalDate defaultValue = LocalDate.of(2001, 7, 31);
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull TemporalFieldInfo dateFieldInfo =
				new DateFieldInfo("active", 5, temporalDescriptor);
		@NotNull String fieldName = dateFieldInfo.getJFXFieldClassName();
		Assert.assertEquals("getJFXFieldClassName() should return DateField",
				"ru.mvk.iluvatar.javafx.field.DateField", fieldName);
	}
}
