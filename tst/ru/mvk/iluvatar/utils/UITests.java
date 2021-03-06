/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.utils;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.loadui.testfx.GuiTest;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.descriptor.field.*;
import ru.mvk.iluvatar.javafx.field.DateField;
import ru.mvk.iluvatar.javafx.field.RealField;
import ru.mvk.iluvatar.javafx.field.RefField;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.StringSupplier;
import ru.mvk.iluvatar.view.View;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public abstract class UITests<Type> extends GuiTest {
	protected static final double DOUBLE_PRECISION = 1e-10;
	@Nullable
	private Type objectUnderTest;

	protected final void setObjectUnderTest(@Nullable Type objectUnderTest) {
		this.objectUnderTest = objectUnderTest;
	}

	@NotNull
	protected final Type getObjectUnderTest() {
		if (objectUnderTest == null) {
			throw new RuntimeException("Testing object is null");
		}
		return objectUnderTest;
	}

	protected final void assertColumnLabelsAreCorrect(@NotNull TableView<?> tableView,
	                                                  @NotNull Iterator<Entry<String,
			                                                  ColumnInfo>> iterator,
	                                                  @NotNull
	                                                  StringSupplier stringSupplier) {
		for (int i = 0; iterator.hasNext(); i++) {
			@Nullable Entry<String, ColumnInfo> entry = iterator.next();
			@NotNull ColumnInfo columnInfo = CommonTestUtils.getEntryValue(entry);
			@NotNull String columnName = columnInfo.getName();
			@NotNull String suppliedColumnName = stringSupplier.apply(columnName);
			assertColumnLabelText(tableView, i, suppliedColumnName);
		}
	}

	protected final void assertFieldLabelsAreCorrect(@NotNull IdGenerator idGenerator,
	                                                 @NotNull Iterator<Entry<String,
			                                                 NamedFieldInfo>> iterator,
	                                                 @NotNull
	                                                 StringSupplier stringSupplier) {
		while (iterator.hasNext()) {
			@Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
			@NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
			@NotNull NamedFieldInfo fieldInfo = CommonTestUtils.getEntryValue(entry);
			@NotNull String fieldLabel = fieldInfo.getName();
			@NotNull String suppliedFieldLabel = stringSupplier.apply(fieldLabel);
			assertFieldLabelIsCorrect(idGenerator, fieldKey, suppliedFieldLabel);
		}
	}

	private void assertFieldLabelIsCorrect(@NotNull IdGenerator idGenerator,
	                                       @NotNull String key,
	                                       @NotNull String expected) {
		@NotNull String id = idGenerator.getLabelId(key);
		@NotNull Label fieldLabel = safeFindById(id);
		@Nullable String fieldLabelText = fieldLabel.getText();
		Assert.assertEquals("Label with id '" + id + "' should contain text " + expected,
				expected, fieldLabelText);
	}

	protected final void assertFieldsHaveCorrectTypes(@NotNull IdGenerator idGenerator,
	                                                  @NotNull Iterator<Entry<String,
			                                                  NamedFieldInfo>> iterator,
	                                                  @NotNull
	                                                  Map<String, Class<? extends Node>>
			                                                  nodeTypes) {
		while (iterator.hasNext()) {
			@Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
			@NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
			@Nullable Class<? extends Node> nodeType = nodeTypes.get(fieldKey);
			if (nodeType == null) {
				throw new RuntimeException("Node type is null");
			}
			assertFieldHasType(idGenerator, fieldKey, nodeType);
		}
	}

	private void assertFieldHasType(@NotNull IdGenerator idGenerator, @NotNull String key,
	                                @NotNull Class<? extends Node> nodeType) {
		@NotNull String id = idGenerator.getFieldId(key);
		@NotNull String typeName = nodeType.getSimpleName();
		@NotNull Node field = safeFindById(id);
		boolean result = nodeType.isInstance(field);
		Assert.assertTrue("Field with id '" + id + "' should be of type '" + typeName + "'",
				result);
	}

	protected final void assertFieldsValues(@NotNull IdGenerator idGenerator,
	                                        @NotNull Iterator<Entry<String,
			                                        NamedFieldInfo>> iterator,
	                                        @NotNull Object entity) {
		while (iterator.hasNext()) {
			@Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
			@NotNull NamedFieldInfo fieldInfo = CommonTestUtils.getEntryValue(entry);
			@NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
			@NotNull Object expectedValue = getFieldValue(entity, fieldKey);
			@NotNull String stringExpectedValue = expectedValue.toString();
			if (fieldInfo instanceof ListFieldInfo) {
				assertListFieldByIdContainsValue(idGenerator, fieldKey, expectedValue);
			} else if (fieldInfo instanceof TemporalFieldInfo) {
				assertDateFieldByKeyHasValue(idGenerator, fieldKey, (LocalDate) expectedValue);
			} else if (fieldInfo instanceof RealFieldInfo) {
				double multiplier =
						Math.pow(10.0, ((RealFieldInfo) fieldInfo).getFractionWidth());
				assertRealFieldByKeyHasValue(idGenerator, fieldKey,
						(double) ((Number) expectedValue).longValue() / multiplier);
			} else if (fieldInfo instanceof SizedFieldInfo) {
				assertTextFieldByKeyContainsText(idGenerator, fieldKey, stringExpectedValue);
			} else {
				assertCheckBoxHasState(idGenerator, fieldKey, (Boolean) expectedValue);
			}
		}
	}

	protected final void assertNodesOrder(@NotNull IdGenerator idGenerator,
	                                      @NotNull Iterator<Entry<String,
			                                      NamedFieldInfo>> iterator) {
		assertFieldsOrder(idGenerator, iterator);
		@NotNull String saveButtonId = idGenerator.getButtonId("Save");
		assertNodeIsFocused(saveButtonId);
		push(KeyCode.TAB);
		@NotNull String cancelButtonId = idGenerator.getButtonId("Cancel");
		assertNodeIsFocused(cancelButtonId);
	}

	private void assertFieldsOrder(@NotNull IdGenerator idGenerator,
	                               @NotNull Iterator<Entry<String,
			                               NamedFieldInfo>> iterator) {
		while (iterator.hasNext()) {
			@Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
			@NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
			@NotNull String fieldId = idGenerator.getFieldId(fieldKey);
			assertNodeIsFocused(fieldId);
			push(KeyCode.TAB);
		}
	}

	private void assertNodeIsFocused(@NotNull String nodeId) {
		@NotNull Node field = safeFindById(nodeId);
		boolean isFieldFocused = field.isFocused();
		Assert.assertTrue("Node with id '" + nodeId + "' should be focused",
				isFieldFocused);
	}

	protected final void assertTextFieldByIdContainsText(@NotNull String id,
	                                                     @NotNull String expected) {
		@NotNull TextField textField = safeFindById(id);
		@Nullable String fieldText = textField.getText();
		Assert.assertEquals("Field with id '" + id + "' should contain text " + expected,
				expected, fieldText);
	}

	private <RefType extends RefAble> void assertListFieldByIdContainsValue(@NotNull
	                                                                        IdGenerator
			                                                                        idGenerator,
	                                                                        @NotNull
	                                                                        String key,
	                                                                        @NotNull Object
			                                                                        expected) {
		@NotNull String id = idGenerator.getFieldId(key);
		@NotNull RefField<?, RefType> listField = safeFindById(id);
		@NotNull RefType fieldValue = getRefFieldValue(listField);
		@NotNull Serializable fieldValueId = fieldValue.getId();
		Assert.assertEquals("Field with id '" + id + "' should contain text " + expected,
				expected, fieldValueId);
	}

	private void assertTextFieldByKeyContainsText(@NotNull IdGenerator idGenerator,
	                                              @NotNull String key,
	                                              @NotNull String expected) {
		@NotNull String id = idGenerator.getFieldId(key);
		assertTextFieldByIdContainsText(id, expected);
	}

	private void assertRealFieldByKeyHasValue(@NotNull IdGenerator idGenerator,
	                                          @NotNull String key,
	                                          double expected) {
		@NotNull String id = idGenerator.getFieldId(key);
		@NotNull RealField realField = safeFindById(id);
		@Nullable String fieldValue = realField.getText();
		if (fieldValue == null) {
			throw new RuntimeException("fieldValue is null");
		}
		double value = Double.parseDouble(fieldValue);
		Assert.assertEquals("Field with id '" + id + "' should contain value " + expected,
				expected, value, DOUBLE_PRECISION);
	}

	private void assertDateFieldByKeyHasValue(@NotNull IdGenerator idGenerator,
	                                          @NotNull String key,
	                                          @NotNull LocalDate expected) {
		@NotNull String id = idGenerator.getFieldId(key);
		@NotNull DateField dateField = safeFindById(id);
		@Nullable LocalDate fieldValue = dateField.getValue();
		Assert.assertEquals("Field with id '" + id + "' should contain value " + expected,
				expected, fieldValue);
	}

	private void assertCheckBoxHasState(@NotNull IdGenerator idGenerator,
	                                    @NotNull String key, boolean expectedState) {
		@NotNull String id = idGenerator.getFieldId(key);
		@NotNull CheckBox checkBox = safeFindById(id);
		boolean checkBoxState = checkBox.isSelected();
		@NotNull String insertion = expectedState ? "" : "not";
		Assert.assertEquals("Checkbox with id '" + id + "' should " + insertion + " be " +
				"checked", expectedState, checkBoxState);
	}

	private <EntityType> void assertColumnLabelText(@NotNull
	                                                TableView<EntityType> tableView,
	                                                int index, @NotNull String expected) {
		@Nullable ObservableList<TableColumn<EntityType, ?>> columnList =
				tableView.getColumns();
		if (columnList == null) {
			throw new RuntimeException("Column list is null");
		}
		@Nullable TableColumn<EntityType, ?> column = columnList.get(index);
		if (column == null) {
			throw new RuntimeException("Column #" + (index + 1) + " is null");
		}
		@Nullable String columnLabel = column.getText();
		Assert.assertEquals("Label of column #" + (index + 1) + " should be correct",
				expected, columnLabel);
	}

	protected final void runAndWait(@NotNull Runnable runnable) {
		try {
			@NotNull FutureTask<?> future = new FutureTask<>(runnable, null);
			Platform.runLater(future);
			future.get();
			sleep(300);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Something went wrong", e);
		}
	}

	protected final void pasteFromClipboardIntoTextField(@NotNull String id) {
		safeRightClickById(id).click("Paste");
	}

	protected final void emptyField(@NotNull String id) {
		safeRightClickById(id).click("Select All");
		type(KeyCode.DELETE);
	}

	protected final void putToClipboard(@NotNull String text) {
		@NotNull StringSelection stringSelection = new StringSelection(text);
		@Nullable Toolkit defaultToolKit = Toolkit.getDefaultToolkit();
		if (defaultToolKit == null) {
			throw new RuntimeException("Default toolkit is null");
		}
		@Nullable Clipboard clipboard = defaultToolKit.getSystemClipboard();
		if (clipboard == null) {
			throw new RuntimeException("System clipboard is null");
		}
		clipboard.setContents(stringSelection, null);
	}

	@NotNull
	protected final <NodeType extends Node> NodeType safeFindById(@NotNull String id) {
		@Nullable NodeType result = GuiTest.find('#' + id);
		if (result == null) {
			throw new RuntimeException("Find result is null");
		}
		return result;
	}

	@NotNull
	protected final String filterNatural(@NotNull String text, int width) {
		@Nullable String result = text.replaceAll("\\D", "");
		if (result == null) {
			throw new RuntimeException("Replace result is null");
		}
		return StringUtils.substring(result, 0, width);
	}

	@NotNull
	protected final String filterInteger(@NotNull String text, int width) {
		@NotNull String result;
		@NotNull String natural;
		int pointPos = text.indexOf('-');
		if (pointPos > -1) {
			natural = text.substring(pointPos + 1);
			result = "-";
		} else {
			natural = text;
			result = "";
		}
		return result + filterNatural(natural, width);
	}

	@NotNull
	protected final String filterReal(@NotNull String text, int integerWidth,
	                                  int fractionWidth) {
		@NotNull String result;
		int pointPos = text.indexOf('.');
		if (pointPos > -1) {
			@NotNull String integer = text.substring(0, pointPos);
			@NotNull String fraction = text.substring(pointPos + 1);
			result = filterInteger(integer, integerWidth) + "." +
					filterNatural(fraction, fractionWidth);
		} else {
			result = filterInteger(text, integerWidth);
		}
		return result;
	}

	@NotNull
	private Object getFieldValue(@NotNull Object entity, @NotNull String fieldKey) {
		@Nullable Object result = getNullableFieldValue(entity, fieldKey);
		if (result == null) {
			throw new RuntimeException("Field value is null");
		}
		return result;
	}

	@Nullable
	private Object getNullableFieldValue(@NotNull Object entity,
	                                     @NotNull String fieldKey) {
		try {
			@Nullable PropertyDescriptor propertyDescriptor =
					PropertyUtils.getPropertyDescriptor(entity, fieldKey);
			if (propertyDescriptor == null) {
				throw new RuntimeException("PropertyDescriptor is null");
			}
			@Nullable Method readMethod = PropertyUtils.getReadMethod(propertyDescriptor);
			if (readMethod == null) {
				throw new RuntimeException("ReadMethod is null");
			}
			return readMethod.invoke(entity);
		} catch (IllegalAccessException | InvocationTargetException |
				NoSuchMethodException e) {
			throw new RuntimeException("Could not read field value");
		}
	}

	@NotNull
	protected final GuiTest safeMoveById(@NotNull String id) {
		@Nullable GuiTest result = move('#' + id);
		if (result == null) {
			throw new RuntimeException("Click result is null");
		}
		return result;
	}

	@NotNull
	protected final GuiTest safeClickById(@NotNull String id) {
		@Nullable GuiTest result = click('#' + id);
		if (result == null) {
			throw new RuntimeException("Click result is null");
		}
		return result;
	}

	@NotNull
	protected final GuiTest safeRightClickById(@NotNull String id) {
		@Nullable GuiTest result = rightClick('#' + id);
		if (result == null) {
			throw new RuntimeException("Click result is null");
		}
		return result;
	}

	@NotNull
	protected <RefType extends RefAble> List<RefType>
	getRefFieldItems(@NotNull RefField<?, RefType> field) {
		@Nullable List<RefType> items = field.getItems();
		if (items == null) {
			throw new RuntimeException("Items are null");
		}
		return items;
	}

	@NotNull
	protected <RefType extends RefAble> RefType
	getRefFieldValue(@NotNull RefField<?, RefType> field) {
		@Nullable RefType fieldValue = field.getValue();
		if (fieldValue == null) {
			throw new RuntimeException("Field value is null");
		}
		return fieldValue;
	}
}
