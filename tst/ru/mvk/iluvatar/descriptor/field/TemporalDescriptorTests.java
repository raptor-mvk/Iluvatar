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

public class TemporalDescriptorTests {
	@Test
	public void constructor_ShouldSetDefaultValue() {
		@NotNull LocalDate defaultValue = LocalDate.of(2005, 10, 2);
		@NotNull DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern("dd-MM-yy");
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull LocalDate temporalDescriptorDefaultValue =
				temporalDescriptor.getDefaultValue();
		Assert.assertEquals("constructor should set correct value of 'defaultValue'",
				defaultValue, temporalDescriptorDefaultValue);
	}

	@Test
	public void constructor_ShouldSetFormatter() {
		@NotNull LocalDateTime defaultValue = LocalDateTime.of(2007, 3, 8, 12, 0, 0);
		@NotNull DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
		@NotNull TemporalDescriptor<LocalDateTime> temporalDescriptor =
				new TemporalDescriptor<>(defaultValue, formatter);
		@NotNull DateTimeFormatter dateFieldFormatter = temporalDescriptor.getFormatter();
		Assert.assertEquals("constructor should set correct value of 'formatter'", formatter,
				dateFieldFormatter);
	}
}
