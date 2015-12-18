/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFieldInfo extends SizedFieldInfoImpl implements
		TemporalFieldInfo<LocalDate> {
	@NotNull
	private final LocalDate defaultValue;
	@NotNull
	private final DateTimeFormatter formatter;

	public DateFieldInfo(@NotNull String name, int width,
	                     @NotNull TemporalDescriptor<LocalDate> temporalDescriptor) {
		super(name, width);
		this.defaultValue = temporalDescriptor.getDefaultValue();
		this.formatter = temporalDescriptor.getFormatter();
	}

	@Override
	@NotNull
	public LocalDate getDefaultValue() {
		return defaultValue;
	}

	@Override
	@NotNull
	public DateTimeFormatter getFormatter() {
		return formatter;
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.DateField";
	}
}
