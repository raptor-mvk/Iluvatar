/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.control.DatePicker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

public class DateField extends DatePicker implements Field<LocalDate> {
	@NotNull
	private static LocalDate DEFAULT_DATE = LocalDate.of(2000, 1, 1);
	@NotNull
	private Consumer<LocalDate> fieldUpdater;
	@NotNull
	private static final DateTimeFormatter dateFormatter =
			DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static void setDefaultDate(@NotNull LocalDate defaultDate) {
		DEFAULT_DATE = defaultDate;
	}

	@NotNull
	public static LocalDate getDefaultDate() {
		return DEFAULT_DATE;
	}

	public DateField() {
		fieldUpdater = (value) -> {
		};
		prepareFocusListener();
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

	private void prepareFocusListener() {
		focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				@NotNull LocalDate fieldValue;
				@Nullable String value = getEditor().getText();
				if (value == null) {
					fieldValue = DEFAULT_DATE;
				} else {
					try {
						fieldValue = LocalDate.parse(value, dateFormatter);
					} catch (DateTimeParseException e) {
						fieldValue = DEFAULT_DATE;
					}
				}
				setValue(fieldValue);
				fieldUpdater.accept(fieldValue);
			}
		});
	}
}
