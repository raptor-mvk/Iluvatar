/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.TemporalDescriptor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateColumnInfo extends PlainColumnInfo {
	@NotNull
	private final DateTimeFormatter formatter;
	@NotNull
	private final String defaultValue;

	public DateColumnInfo(@NotNull String name,
	                      @NotNull TemporalDescriptor<LocalDate> temporalDescriptor) {
		super(name, temporalDescriptor.getWidth());
		formatter = temporalDescriptor.getFormatter();
		defaultValue = temporalDescriptor.getDefaultValue().format(formatter);
	}

	@NotNull
	@Override
	public ViewFormatter getViewFormatter() {
		return (value) -> {
			@NotNull String result = defaultValue;
			if (value instanceof LocalDate) {
				result = ((LocalDate) value).format(formatter);
			}
			return result;
		};
	}

	@NotNull
	@Override
	public Pos getJFXAlignment() {
		return Pos.CENTER;
	}
}
