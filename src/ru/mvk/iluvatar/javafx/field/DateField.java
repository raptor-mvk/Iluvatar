/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import javafx.scene.control.DatePicker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.TemporalFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

public class DateField extends DatePicker implements Field<LocalDate> {
	@NotNull
	private Consumer<LocalDate> fieldUpdater;

	public DateField(@NotNull TemporalFieldInfo<LocalDate> fieldInfo) {
		fieldUpdater = (value) -> {
		};
		prepareFocusListener(fieldInfo);
	}

	@Override
	public void setFieldUpdater(@NotNull Consumer<LocalDate> fieldUpdater) {
		this.fieldUpdater = fieldUpdater;
	}

	@Override
	public void setFieldValue(@NotNull Object value) {
		if (value instanceof LocalDate) {
			setValue((LocalDate) value);
		} else {
			throw new IluvatarRuntimeException("DateField: incorrect value type");
		}
	}

	private void prepareFocusListener(@NotNull TemporalFieldInfo<LocalDate> fieldInfo) {
		@NotNull LocalDate defaultDate = fieldInfo.getDefaultValue();
		@NotNull DateTimeFormatter formatter = fieldInfo.getFormatter();
		focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				@NotNull LocalDate fieldValue;
				@Nullable String value = getEditor().getText();
				if (value == null) {
					fieldValue = defaultDate;
				} else {
					try {
						fieldValue = LocalDate.parse(value, formatter);
					} catch (DateTimeParseException e) {
						fieldValue = defaultDate;
					}
				}
				setValue(fieldValue);
				fieldUpdater.accept(fieldValue);
			}
		});
	}
}
