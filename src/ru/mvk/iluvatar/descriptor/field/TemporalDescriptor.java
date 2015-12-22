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
	private final int width;

	public TemporalDescriptor(@NotNull Type defaultValue,
	                          @NotNull String pattern) {
		this.defaultValue = defaultValue;
		formatter = DateTimeFormatter.ofPattern(pattern);
		width = pattern.length();
	}

	@NotNull
	public Type getDefaultValue() {
		return defaultValue;
	}

	@NotNull
	public DateTimeFormatter getFormatter() {
		return formatter;
	}

	public int getWidth() {
		return width;
	}
}
