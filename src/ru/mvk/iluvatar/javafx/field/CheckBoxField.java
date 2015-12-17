/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Consumer;

public class CheckBoxField extends CheckBox implements Field<Boolean> {
	@NotNull
	private Consumer<Boolean> fieldUpdater;

	public CheckBoxField() {
		fieldUpdater = (value) -> {
		};
		@Nullable BooleanProperty fieldSelectedProperty = selectedProperty();
		if (fieldSelectedProperty == null) {
			throw new IluvatarRuntimeException("CheckBoxField: selectedProperty is null");
		}
		fieldSelectedProperty.addListener((observable, oldValue, newValue) -> {
			fieldUpdater.accept(newValue);
		});
	}

	@Override
	public void setFieldUpdater(@NotNull Consumer<Boolean> fieldUpdater) {
		this.fieldUpdater = fieldUpdater;
	}

	@Override
	public void setFieldValue(@NotNull Object value) {
		if (value instanceof Boolean) {
			setSelected((Boolean) value);
		} else {
			throw new IluvatarRuntimeException("CheckBoxField: incorrect value type");
		}
	}
}
