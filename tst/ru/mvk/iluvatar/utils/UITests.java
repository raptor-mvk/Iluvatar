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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.loadui.testfx.GuiTest;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.descriptor.field.ListFieldInfo;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;
import ru.mvk.iluvatar.descriptor.field.RefAble;
import ru.mvk.iluvatar.descriptor.field.SizedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.javafx.field.RefField;
import ru.mvk.iluvatar.view.StringSupplier;
import ru.mvk.iluvatar.view.View;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  protected final void
  assertColumnLabelsAreCorrect(@NotNull TableView<?> tableView,
                               @NotNull Iterator<Entry<String, ColumnInfo>> iterator,
                               @NotNull StringSupplier stringSupplier) {
    for (int i = 0; iterator.hasNext(); i++) {
      @Nullable Entry<String, ColumnInfo> entry = iterator.next();
      @NotNull ColumnInfo columnInfo = CommonTestUtils.getEntryValue(entry);
      @NotNull String columnName = columnInfo.getName();
      @NotNull String suppliedColumnName = stringSupplier.apply(columnName);
      assertColumnLabelText(tableView, i, suppliedColumnName);
    }
  }

  protected final void
  assertFieldLabelsAreCorrect(@NotNull View<?> view,
                              @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator,
                              @NotNull StringSupplier stringSupplier) {
    while (iterator.hasNext()) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      @NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
      @NotNull NamedFieldInfo fieldInfo = CommonTestUtils.getEntryValue(entry);
      @NotNull String fieldLabel = fieldInfo.getName();
      @NotNull String suppliedFieldLabel = stringSupplier.apply(fieldLabel);
      assertFieldLabelIsCorrect(view, fieldKey, suppliedFieldLabel);
    }
  }

  private void assertFieldLabelIsCorrect(@NotNull View<?> view,
                                         @NotNull String key,
                                         @NotNull String expected) {
    @NotNull String id = view.getLabelId(key);
    @NotNull Label fieldLabel = safeFindById(id);
    @Nullable String fieldLabelText = fieldLabel.getText();
    Assert.assertEquals("Label with id '" + id + "' should contain text " + expected,
                           expected, fieldLabelText);
  }

  protected final void
  assertFieldsHaveCorrectTypes(@NotNull View<?> view,
                               @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator,
                               @NotNull Map<String, Class<? extends Node>> nodeTypes) {
    while (iterator.hasNext()) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      @NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
      @Nullable Class<? extends Node> nodeType = nodeTypes.get(fieldKey);
      if (nodeType == null) {
        throw new RuntimeException("Node type is null");
      }
      assertFieldHasType(view, fieldKey, nodeType);
    }
  }

  private void assertFieldHasType(@NotNull View<?> view, @NotNull String key,
                                  @NotNull Class<? extends Node> nodeType) {
    @NotNull String id = view.getFieldId(key);
    @NotNull String typeName = nodeType.getSimpleName();
    @NotNull Node field = safeFindById(id);
    boolean result = nodeType.isInstance(field);
    Assert.assertTrue("Field with id '" + id + "' should be of type '" + typeName + "'",
                         result);
  }

  protected final void
  assertFieldsValues(@NotNull View<?> view,
                     @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator,
                     @NotNull Object entity) {
    while (iterator.hasNext()) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      @NotNull NamedFieldInfo fieldInfo = CommonTestUtils.getEntryValue(entry);
      @NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
      @NotNull Object expectedValue = getFieldValue(entity, fieldKey);
      @NotNull String stringExpectedValue = expectedValue.toString();
      if (fieldInfo instanceof ListFieldInfo) {
        assertListFieldByIdContainsValue(view, fieldKey, expectedValue);
      } else if (fieldInfo instanceof SizedFieldInfo) {
        assertTextFieldByKeyContainsText(view, fieldKey, stringExpectedValue);
      } else {
        assertCheckBoxHasState(view, fieldKey, (Boolean) expectedValue);
      }
    }
  }

  protected final void
  assertNodesOrder(@NotNull View<?> view,
                   @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator) {
    assertFieldsOrder(view, iterator);
    @NotNull String saveButtonId = view.getSaveButtonId();
    assertNodeIsFocused(saveButtonId);
    push(KeyCode.TAB);
    @NotNull String cancelButtonId = view.getCancelButtonId();
    assertNodeIsFocused(cancelButtonId);
  }

  private void
  assertFieldsOrder(@NotNull View<?> view,
                    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator) {
    while (iterator.hasNext()) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      @NotNull String fieldKey = CommonTestUtils.getEntryKey(entry);
      @NotNull String fieldId = view.getFieldId(fieldKey);
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

  private <RefType extends RefAble> void
  assertListFieldByIdContainsValue(@NotNull View<?> view,
                                   @NotNull String key, @NotNull Object expected) {
    @NotNull String id = view.getFieldId(key);
    @NotNull RefField<?, RefType> listField = safeFindById(id);
    @Nullable RefType fieldValue = listField.getValue();
    if (fieldValue==null) {
      throw new RuntimeException("Field value is null");
    }
    @NotNull Serializable fieldValueId = fieldValue.getId();
    Assert.assertEquals("Field with id '" + id + "' should contain text " + expected,
                           expected, fieldValueId);
  }

  private void assertTextFieldByKeyContainsText(@NotNull View<?> view,
                                                @NotNull String key,
                                                @NotNull String expected) {
    @NotNull String id = view.getFieldId(key);
    assertTextFieldByIdContainsText(id, expected);
  }

  private void assertCheckBoxHasState(@NotNull View<?> view, @NotNull String key,
                                      boolean expectedState) {
    @NotNull String id = view.getFieldId(key);
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
  protected final String filterNatural(@NotNull String text) {
    @Nullable String result = text.replaceAll("\\D", "");
    if (result == null) {
      throw new RuntimeException("Replace result is null");
    }
    return result;
  }

  @NotNull
  protected final String filterInteger(@NotNull String text) {
    @NotNull String result;
    int pointPos = text.indexOf('-');
    if (pointPos > -1) {
      @NotNull String natural = text.substring(pointPos + 1);
      result = "-" + filterNatural(natural);
    } else {
      result = filterNatural(text);
    }
    return result;
  }

  @NotNull
  protected final String filterReal(@NotNull String text) {
    @NotNull String result;
    int pointPos = text.indexOf('.');
    if (pointPos > -1) {
      @NotNull String integer = text.substring(0, pointPos - 1);
      @NotNull String fraction = text.substring(pointPos + 1);
      result = filterInteger(integer) + "." + filterNatural(fraction);
    } else {
      result = filterInteger(text);
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
}
