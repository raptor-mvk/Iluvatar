/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.ViewInfo;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;
import ru.mvk.iluvatar.descriptor.field.SizedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.javafx.field.Field;
import ru.mvk.iluvatar.javafx.field.RefList;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.StringSupplier;
import ru.mvk.iluvatar.view.View;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class JFXView<EntityType> implements View<EntityType> {
	@NotNull
	private final JFXEntityForm<EntityType> entityForm;
	@NotNull
	private final JFXButtonRow buttonRow;
	@NotNull
	private final VBox root;
	@NotNull
	private final String saveButtonName = "Save";
	@NotNull
	private final String cancelButtonName = "Cancel";
	@NotNull
	private final ViewInfo<EntityType> viewInfo;

	/* TODO extract private methods */
	public JFXView(@NotNull ViewInfo<EntityType> viewInfo,
	               @NotNull StringSupplier stringSupplier,
	               @NotNull IdGenerator idGenerator) {
		this.viewInfo = viewInfo;
		entityForm = new JFXEntityForm<>(viewInfo, stringSupplier, idGenerator);
		buttonRow = new JFXButtonRow(stringSupplier, idGenerator);
		root = new VBox();
		buttonRow.addButton(saveButtonName);
		buttonRow.addButton(cancelButtonName);
		root.getChildren().addAll(entityForm, buttonRow);
		setKeyListeners();
	}

	@Nullable
	@Override
	public Parent getView(@NotNull EntityType entity, boolean isNewEntity) {
		prepareFieldValues(entity);
		Platform.runLater(entityForm::focusOnFirstField);
		return root;
	}

	@Override
	public void setSaveButtonHandler(@NotNull Runnable handler) {
		buttonRow.setButtonHandler(saveButtonName, handler);
	}

	@Override
	public void setCancelButtonHandler(@NotNull Runnable handler) {
		buttonRow.setButtonHandler(cancelButtonName, handler);
	}

	private void prepareFieldValues(@NotNull EntityType entity) {
		@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
		while (iterator.hasNext()) {
			@Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
			if (entry != null) {
				@Nullable String fieldKey = entry.getKey();
				if (fieldKey != null) {
					setFieldValue(fieldKey, entity);
				}
			}
		}
	}

	private void setFieldValue(@NotNull String fieldKey,
	                           @NotNull EntityType object) {
		try {
			@NotNull Node field = entityForm.getFieldNode(fieldKey);
			if (field instanceof RefList) {
				((RefList) field).reload();
			}
			@NotNull PropertyDescriptor propertyDescriptor =
					getPropertyDescriptor(fieldKey, object);
			@NotNull Object value = getFieldValue(propertyDescriptor, object);
			if (field instanceof Field) {
				((Field<?>) field).setFieldValue(value);
			} else {
				throw new IluvatarRuntimeException("JFXView: Incorrect field type");
			}
			setFieldUpdater(field, propertyDescriptor, object);
		} catch (IllegalAccessException | InvocationTargetException |
				NoSuchMethodException e) {
			throw new IluvatarRuntimeException("JFXView: Could not access field '" +
					fieldKey + '\'');
		}
	}

	@NotNull
	private PropertyDescriptor getPropertyDescriptor(@NotNull String fieldKey,
	                                                 @NotNull EntityType object)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		@Nullable PropertyDescriptor propertyDescriptor =
				PropertyUtils.getPropertyDescriptor(object, fieldKey);
		if (propertyDescriptor == null) {
			throw new IluvatarRuntimeException("JFXView: could not access field '" +
					fieldKey + "'");
		}
		return propertyDescriptor;
	}

	@NotNull
	private Object getFieldValue(@NotNull PropertyDescriptor propertyDescriptor,
	                             @NotNull EntityType object)
			throws IllegalAccessException, InvocationTargetException {
		@Nullable Method readMethod = PropertyUtils.getReadMethod(propertyDescriptor);
		if (readMethod == null) {
			throw new IluvatarRuntimeException("JFXView: could not access field");
		}
		@Nullable Object result = readMethod.invoke(object);
		if (result == null) {
			throw new IluvatarRuntimeException("JFXView: field has null value");
		}
		return result;
	}

	private void setFieldUpdater(@NotNull Node field,
	                             @NotNull PropertyDescriptor propertyDescriptor,
	                             @NotNull EntityType object) {
		@Nullable Method writeMethod = PropertyUtils.getWriteMethod(propertyDescriptor);
		if (field instanceof Field<?>) {
			((Field<?>) field).setFieldUpdater((fieldValue) -> {
				try {
					if (writeMethod == null) {
						throw new IllegalAccessException();
					}
					writeMethod.invoke(object, fieldValue);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new IluvatarRuntimeException("JFXView: Could not access field " +
							propertyDescriptor.getName());
				}
			});
		}
	}

	private void setKeyListeners() {
		root.setOnKeyPressed((event) -> {
			@NotNull KeyCode keyCode = event.getCode();
			if (keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE) {
				event.consume();
			}
		});
		root.setOnKeyReleased((event) -> {
			@NotNull KeyCode keyCode = event.getCode();
			if (keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE) {
				event.consume();
				if (keyCode == KeyCode.ENTER) {
					buttonRow.requestButtonFocus(saveButtonName);
					buttonRow.fireButton(saveButtonName);
				} else if (keyCode == KeyCode.ESCAPE) {
					buttonRow.fireButton(cancelButtonName);
				}
			}
		});
	}
}
