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
	private final VBox root;
	private boolean newEntity;
	@NotNull
	private final Button saveButton;
	@NotNull
	private final Button cancelButton;
	@NotNull
	private final ViewInfo<EntityType> viewInfo;
	@NotNull
	private Consumer<Boolean> saveButtonHandler = (isNewEntity) -> {
	};
	@NotNull
	private Runnable cancelButtonHandler = () -> {
	};
	@NotNull
	private final StringSupplier stringSupplier;
	@NotNull
	private final IdGenerator idGenerator;

	/* TODO extract private methods */
	public JFXView(@NotNull ViewInfo<EntityType> viewInfo,
	               @NotNull StringSupplier stringSupplier,
	               @NotNull IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
		this.viewInfo = viewInfo;
		this.stringSupplier = stringSupplier;
		saveButton = prepareSaveButton();
		cancelButton = prepareCancelButton();
		entityForm = new JFXEntityForm<>(viewInfo, stringSupplier, idGenerator);
		root = new VBox();
		@NotNull HBox buttonsRow = new HBox();
		buttonsRow.getChildren().addAll(saveButton, cancelButton);
		root.getChildren().addAll(entityForm, buttonsRow);
		setKeyListeners();
	}

	@Nullable
	@Override
	public Parent getView(@NotNull EntityType entity, boolean isNewEntity) {
		newEntity = isNewEntity;
		prepareFieldValues(entity);
		Platform.runLater(entityForm::focusOnFirstField);
		return root;
	}

	@Override
	public void setSaveButtonHandler(@NotNull Consumer<Boolean> handler) {
		saveButtonHandler = handler;
		saveButton.setOnAction(event -> handler.accept(newEntity));
	}

	@Override
	public void setCancelButtonHandler(@NotNull Runnable handler) {
		cancelButtonHandler = handler;
		cancelButton.setOnAction(event -> handler.run());
	}

	@NotNull
	private Button prepareSaveButton() {
		@NotNull String buttonName = "Save";
		@NotNull String saveButtonId = idGenerator.getButtonId(buttonName);
		@NotNull String saveButtonCaption = stringSupplier.apply(buttonName);
		@NotNull Button result = new Button(saveButtonCaption);
		result.setId(saveButtonId);
		return result;
	}

	@NotNull
	private Button prepareCancelButton() {
		@NotNull String buttonName = "Cancel";
		@NotNull String cancelButtonId = idGenerator.getButtonId(buttonName);
		@NotNull String cancelButtonCaption = stringSupplier.apply(buttonName);
		@NotNull Button result = new Button(cancelButtonCaption);
		result.setId(cancelButtonId);
		return result;
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
					saveButton.requestFocus();
					saveButtonHandler.accept(newEntity);
				} else if (keyCode == KeyCode.ESCAPE) {
					cancelButtonHandler.run();
				}
			}
		});
	}
}
