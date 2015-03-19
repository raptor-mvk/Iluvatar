/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

public class CheckBoxFieldUITests extends UITests<CheckBoxField> {
  @NotNull
  private static final String ID = "fieldId";
  @NotNull
  private final FieldValueTester<Boolean> fieldValueTester = new FieldValueTester<>();

  @Test
  public void click_ShouldSetValueToFalse() {
    safeClickById(ID);
    @Nullable Boolean value = fieldValueTester.getValue();
    Assert.assertEquals("click on checkbox should set value to false", false, value);
  }

  @Test
  public void clickTwice_ShouldSetValueToTrue() {
    safeClickById(ID);
    safeClickById(ID);
    @Nullable Boolean value = fieldValueTester.getValue();
    Assert.assertEquals("click twice on checkbox should set value to true", true, value);
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull CheckBoxField field = new CheckBoxField();
    field.setFieldUpdater(fieldValueTester::setValue);
    fieldValueTester.setValue(true);
    field.setSelected(true);
    field.setId(ID);
    return field;
  }
}
