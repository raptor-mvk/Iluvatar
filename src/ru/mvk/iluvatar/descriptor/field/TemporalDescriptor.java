/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

public class TemporalDescriptor<Type> {
	@NotNull
	private final Type defaultValue;
	@NotNull
	private final DateTimeFormatter formatter;

	public TemporalDescriptor(@NotNull Type defaultValue,
	                          @NotNull DateTimeFormatter formatter) {
		this.defaultValue = defaultValue;
		this.formatter = formatter;
	}

	@NotNull
	public Type getDefaultValue() {
		return defaultValue;
	}

	@NotNull
	public DateTimeFormatter getFormatter() {
		return formatter;
	}
}
