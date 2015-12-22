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

public class TemporalDescriptorUnitTests {
	@Test
	public void constructor_ShouldSetDefaultValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(2005, 10, 2);
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, "dd-MM-yy");
		@NotNull LocalDate temporalDescriptorDefaultValue =
				temporalDescriptor.getDefaultValue();
		Assert.assertEquals("constructor should set correct value of 'defaultValue'",
				defaultValue, temporalDescriptorDefaultValue);
	}

	@Test
	public void constructor_ShouldSetFormatter() {
		@NotNull LocalDateTime defaultValue = LocalDateTime.of(2007, 3, 8, 12, 0, 0);
		@NotNull String pattern = "yy.MM.dd hh:mm:ss";
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		@NotNull TemporalDescriptor<LocalDateTime> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, pattern);
		@NotNull String expectedValue = defaultValue.format(formatter);
		@NotNull DateTimeFormatter dateFieldFormatter = temporalDescriptor.getFormatter();
		@NotNull String value = defaultValue.format(dateFieldFormatter);
		Assert.assertEquals("constructor should set correct value of 'formatter'",
				expectedValue, value);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		@NotNull LocalDateTime defaultValue = LocalDateTime.of(2007, 3, 8, 12, 0, 0);
		@NotNull String pattern = "yyyy/dd/MM";
		int expectedWidth = pattern.length();
		@NotNull TemporalDescriptor<LocalDateTime> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, pattern);
		int temporalDescriptorWidth = temporalDescriptor.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", expectedWidth,
				temporalDescriptorWidth);
	}
}
