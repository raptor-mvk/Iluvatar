/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.ViewInfo;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;
import ru.mvk.iluvatar.descriptor.field.SizedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.StringSupplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

class JFXEntityForm<EntityType> extends GridPane {
	@NotNull
	private final LinkedHashMap<String, Node> fields = new LinkedHashMap<>();
	@NotNull
	private final ViewInfo<EntityType> viewInfo;
	@NotNull
	private final StringSupplier stringSupplier;
	@NotNull
	private final IdGenerator idGenerator;

	JFXEntityForm(@NotNull ViewInfo<EntityType> viewInfo,
	              @NotNull StringSupplier stringSupplier,
	              @NotNull IdGenerator idGenerator) {
		this.viewInfo = viewInfo;
		this.stringSupplier = stringSupplier;
		this.idGenerator = idGenerator;
		setGaps();
		prepareView();
	}

	void focusOnFirstField() {
		@NotNull Iterator<Map.Entry<String, NamedFieldInfo>> iterator =
				viewInfo.getIterator();
		if (iterator.hasNext()) {
			@Nullable Map.Entry<String, NamedFieldInfo> entry = iterator.next();
			if (entry != null) {
				@Nullable String fieldKey = entry.getKey();
				if (fieldKey != null) {
					@Nullable Node firstField = fields.get(fieldKey);
					if (firstField != null) {
						Platform.runLater(firstField::requestFocus);
					}
				}
			}
		}
	}

	@NotNull
	Node getFieldNode(@NotNull String fieldKey) {
		@Nullable Node result = fields.get(fieldKey);
		if (result == null) {
			@NotNull String errorMessage =
					String.format("JFXView: field '%s' was not found", fieldKey);
			throw new IluvatarRuntimeException(errorMessage);
		}
		return result;
	}

	private void setGaps() {
		// 5.0 is empirically selected gap and padding
		setHgap(5.0);
		setVgap(5.0);
		setPadding(new Insets(5.0));
	}

	private void prepareView() {
		@NotNull Iterator<Map.Entry<String, NamedFieldInfo>> iterator =
				viewInfo.getIterator();
		for (int i = 0; iterator.hasNext(); i++) {
			@Nullable Map.Entry<String, NamedFieldInfo> entry = iterator.next();
			if (entry != null) {
				@Nullable NamedFieldInfo fieldInfo = entry.getValue();
				@Nullable String fieldKey = entry.getKey();
				if ((fieldKey != null) && (fieldInfo != null)) {
					prepareLabel(i, fieldKey);
					prepareField(i, fieldKey, fieldInfo);
				}
			}
		}
	}

	private void prepareLabel(int index, @NotNull String fieldKey) {
		@NotNull String labelId = idGenerator.getLabelId(fieldKey);
		@NotNull NamedFieldInfo fieldInfo = viewInfo.getFieldInfo(fieldKey);
		@NotNull String fieldLabel = fieldInfo.getName();
		@NotNull String suppliedFieldLabel = stringSupplier.apply(fieldLabel);
		@NotNull Label label = new Label(suppliedFieldLabel);
		label.setId(labelId);
		add(label, 0, index);
	}

	private void prepareField(int index, @NotNull String fieldKey,
	                          @NotNull NamedFieldInfo fieldInfo) {
		@NotNull Node field = getField(fieldInfo);
		@NotNull String fieldId = idGenerator.getFieldId(fieldKey);
		field.setId(fieldId);
		fields.put(fieldKey, field);
		add(field, 1, index);
	}

	@NotNull
	private Node getField(@NotNull NamedFieldInfo fieldInfo) {
		@Nullable Object fieldInstance = null;
		@Nullable String fieldClassName = fieldInfo.getJFXFieldClassName();
		@Nullable Class<?> fieldClass;
		try {
			fieldClass = Class.forName(fieldClassName);
		} catch (ClassNotFoundException e) {
			throw new IluvatarRuntimeException("FieldUtils: Could not load class " +
					fieldClassName);
		}
		if (fieldClass != null) {
			if (fieldInfo instanceof SizedFieldInfo) {
				fieldInstance = instantiateSizedField(fieldClass, (SizedFieldInfo) fieldInfo);
			} else {
				fieldInstance = instantiateNamedField(fieldClass);
			}
		}
		if (!(fieldInstance instanceof Node)) {
			throw new IluvatarRuntimeException("JFXView: instantiated field is not " +
					"JavaFX Node");
		}
		return (Node) fieldInstance;
	}

	@NotNull
	private Object instantiateSizedField(@NotNull Class<?> fieldClass,
	                                     @NotNull SizedFieldInfo fieldInfo) {
		@NotNull Object result;
		@Nullable Class<?> fieldInfoClass = fieldInfo.getClass();
		if (fieldInfoClass == null) {
			throw new IluvatarRuntimeException("JFXView: fieldInfo class is null");
		}
		@Nullable Constructor fieldConstructor =
				ConstructorUtils.getMatchingAccessibleConstructor(fieldClass, fieldInfoClass);
		if (fieldConstructor == null) {
			throw new IluvatarRuntimeException("Could not get constructor for " + fieldClass);
		}
		try {
			result = fieldConstructor.newInstance(fieldInfo);
		} catch (InstantiationException | IllegalAccessException |
				InvocationTargetException e) {
			throw new IluvatarRuntimeException("JFXView: Could not instantiate field for " +
					fieldClass);
		}
		return result;
	}

	@NotNull
	private Object instantiateNamedField(@NotNull Class<?> fieldClass) {
		@NotNull Object result;
		@Nullable Constructor fieldConstructor =
				ConstructorUtils.getMatchingAccessibleConstructor(fieldClass);
		if (fieldConstructor == null) {
			throw new IluvatarRuntimeException("Could not get constructor for " + fieldClass);
		}
		try {
			result = fieldConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException |
				InvocationTargetException e) {
			throw new IluvatarRuntimeException("JFXView: Could not instantiate field for " +
					fieldClass);
		}
		return result;
	}
}
