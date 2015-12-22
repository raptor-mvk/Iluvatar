/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
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
		setAllWidths(fieldInfo.getWidth());
		prepareFocusListener(fieldInfo);
		prepareStringConverter(fieldInfo);
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

	private void setAllWidths(int length) {
		// 0.75 is a ratio of conversion from font size to average letter width
		@NotNull Font defaultFont = Font.getDefault();
		double width = defaultFont.getSize() * (0.7 * length + 1.5);
		setMinWidth(width);
		setMaxWidth(width);
		setMaxWidth(width);
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
			} else {
				Platform.runLater(getEditor()::selectAll);
			}
		});
	}

	private void prepareStringConverter(@NotNull TemporalFieldInfo<LocalDate> fieldInfo) {
		@NotNull StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			@NotNull
			DateTimeFormatter dateFormatter = fieldInfo.getFormatter();
			@NotNull
			LocalDate defaultDate = fieldInfo.getDefaultValue();

			@NotNull
			@Override
			public String toString(@Nullable LocalDate date) {
				@NotNull String result = "";
				if (date != null) {
					result = dateFormatter.format(date);
				}
				return result;
			}

			@NotNull
			@Override
			public LocalDate fromString(@Nullable String string) {
				@NotNull LocalDate result = defaultDate;
				if (string != null && !string.isEmpty()) {
					result = LocalDate.parse(string, dateFormatter);
				}
				return result;
			}
		};
		setConverter(converter);
	}
}
