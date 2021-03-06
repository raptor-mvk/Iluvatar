/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Consumer;

abstract class SizedTextField<Type> extends TextField implements Field<Type> {
	@NotNull
	private Consumer<Type> fieldUpdater = (value) -> {
	};
	@NotNull
	private final Class<Type> type;
	private boolean badValue = false;
	private boolean needCaretPos = false;
	private boolean needCheck = true;
	private int prevCaretPos = -1;
	private final int maxLength;

	SizedTextField(int maxLength, @NotNull Class<Type> type) {
		if (maxLength <= 0) {
			throw new IluvatarRuntimeException("SizedTextField: non-positive width");
		}
		this.maxLength = maxLength;
		// force double length for too short fields
		if (maxLength > 5) {
			setAllWidths(maxLength);
		} else {
			setAllWidths(maxLength * 2);
		}
		this.type = type;
		setKeyListeners();
		setChangeListener();
	}

	private void setAllWidths(int length) {
		// 0.75 is a ratio of conversion from font size to average letter width
		@NotNull Font defaultFont = Font.getDefault();
		double width = defaultFont.getSize() * (0.7 * length + 1.5);
		setMinWidth(width);
		setMaxWidth(width);
		setMaxWidth(width);
	}

	final int getMaxLength() {
		return maxLength;
	}

	@NotNull
	final Class<Type> getType() {
		return type;
	}

	abstract boolean check(@NotNull String value);

	abstract Type convertValue(@NotNull String value);

	String valueToString(@NotNull Type value) {
		return value.toString();
	}

	private void setKeyListeners() {
		setOnKeyPressed((event) -> prevCaretPos = getCaretPosition());
		setOnKeyReleased((event) -> {
			if (needCaretPos) {
				positionCaret(prevCaretPos);
				needCaretPos = false;
			}
		});
	}

	private void setChangeListener() {
		@Nullable StringProperty fieldTextProperty = textProperty();
		if (fieldTextProperty == null) {
			throw new IluvatarRuntimeException("SizedTextField: textProperty is null");
		}
		fieldTextProperty.addListener((obsValue, oldValue, newValue) -> {
			if (needCheck) {
				if (badValue) {
					if (needCaretPos) {
						positionCaret(prevCaretPos);
						needCaretPos = false;
					}
					badValue = false;
				} else {
					if (!check(newValue)) {
						badValue = true;
						needCaretPos = true;
						setText(oldValue);
					} else {
						@NotNull Type fieldValue = convertValue(newValue);
						fieldUpdater.accept(fieldValue);
					}
				}
			}
		});
	}

	@Override
	public final void setFieldUpdater(@NotNull Consumer<Type> fieldUpdater) {
		this.fieldUpdater = fieldUpdater;
	}

	@Override
	public final void setFieldValue(@NotNull Object value) {
		if (type.isInstance(value)) {
			@NotNull String text = valueToString(type.cast(value));
			needCheck = false;
			setText(text);
			needCheck = true;
		} else {
			throw new IluvatarRuntimeException("SizedTextField: incorrect value type");
		}
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		selectAll();
	}
}
