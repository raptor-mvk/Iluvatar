/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.RealFieldInfo;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

@Deprecated
@Ignore
public class RealFieldUITests extends UITests<RealField<?>> {
  private static final int MAX_LENGTH = 8;
  @NotNull
  private static final String ID = "fieldId";
  @NotNull
  private final FieldValueTester<Double> fieldValueTester = new FieldValueTester<>();

  @Test
  public void input_ShouldSetValue() {
    double input = -824.346;
    @Nullable String inputText = Double.toString(input);
    if (inputText == null) {
      throw new RuntimeException("Input text is null");
    }
    safeClickById(ID).type(inputText);
    @Nullable Double fieldValue = fieldValueTester.getValue();
    if (fieldValue == null) {
      fieldValue = Double.NaN;
    }
    Assert.assertEquals("input real number should set correct value", input, fieldValue,
                           DOUBLE_PRECISION);
  }

  @Test
  public void inputMinus_ShouldSetValueToZero() {
    @NotNull String inputText = "-";
    safeClickById(ID).type(inputText);
    @Nullable Double fieldValue = fieldValueTester.getValue();
    if (fieldValue == null) {
      fieldValue = Double.NaN;
    }
    Assert.assertEquals("input \"-\" should set value to zero", 0.0, fieldValue,
                           DOUBLE_PRECISION);
  }

  @Test
  public void inputPoint_ShouldSetValueToZero() {
    @NotNull String inputText = ".";
    safeClickById(ID).type(inputText);
    @Nullable Double fieldValue = fieldValueTester.getValue();
    if (fieldValue == null) {
      fieldValue = Double.NaN;
    }
    Assert.assertEquals("input \".\" should set value to zero", 0.0, fieldValue,
                           DOUBLE_PRECISION);
  }

  @Test
  public void inputMinusPoint_ShouldSetValueToZero() {
    @NotNull String inputText = "-.";
    safeClickById(ID).type(inputText);
    @Nullable Double fieldValue = fieldValueTester.getValue();
    if (fieldValue == null) {
      fieldValue = Double.NaN;
    }
    Assert.assertEquals("input \"-.\" should set value to zero", 0.0, fieldValue,
                           DOUBLE_PRECISION);
  }

  @Test
  public void emptyField_SetValueToZero() {
    @NotNull String inputText = "2352.15";
    safeClickById(ID).type(inputText);
    emptyField(ID);
    @Nullable Double fieldValue = fieldValueTester.getValue();
    if (fieldValue == null) {
      fieldValue = Double.NaN;
    }
    Assert.assertEquals("empty field should set value to zero", 0.0, fieldValue,
                           DOUBLE_PRECISION);
  }

  @Test
  public void shortRealInput_FieldShouldContainWholeText() {
    @NotNull String inputText = "-532.45";
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, inputText);
  }

  @Test
  public void longRealInput_FieldShouldContainTruncatedText() {
    @NotNull String inputText = "457817257.239879865443";
    @NotNull String resultText = inputText.substring(0, MAX_LENGTH);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortMixedInput_FieldShouldContainOnlyNumber() {
    @NotNull String inputText = "323trainer.34validation7";
    @NotNull String resultText = filterReal(inputText);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void longMixedInput_FieldShouldContainOnlyNumberTruncatedToMaxLength() {
    @NotNull String inputText = "-pixel3612Negotiation.3543Tribune352.LABOR2.3gravity";
    @NotNull String filteredText = filterReal(inputText);
    @NotNull String resultText = filteredText.substring(0, MAX_LENGTH);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortRealPasteFromClipboard_FieldShouldContainWholeText() {
    @NotNull String clipboardText = "-255.26";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, clipboardText);
  }

  @Test
  public void longRealPasteFromClipboard_FieldShouldContainNoText() {
    @NotNull String clipboardText = "234523.97692963";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void shortMixedPasteFromClipboard_FieldShouldContainNoText() {
    @NotNull String clipboardText = "94.s36l3";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void longMixedPasteFromClipboard_FieldShouldContainNoText() {
    putToClipboard("46bacteria432organizer6.trouble322693liberty3953396");
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void inputIntoFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "436.4798";
    safeClickById(ID).type(inputText);
    type("7");
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    int expectedCaretPosition = inputText.length();
    Assert.assertEquals("input into filled field should not move caret",
                           expectedCaretPosition, caretPosition);
  }

  @Test
  public void inputWrongCharacterInsideText_ShouldNotMoveCaret() {
    @NotNull String inputText = "23.3";
    safeClickById(ID).type(inputText);
    type(KeyCode.LEFT);
    type("a");
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    int expectedCaretPosition = inputText.length() - 1;
    Assert.assertEquals("input wrong character inside text should not move caret",
                           expectedCaretPosition, caretPosition);
  }

  @Test
  public void pasteFromClipboardIntoFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "-346.345";
    putToClipboard(inputText);
    safeClickById(ID).type(inputText);
    type(KeyCode.HOME);
    pasteFromClipboardIntoTextField(ID);
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    Assert.assertEquals("paste from clipboard into filled field should not move caret", 0,
                           caretPosition);
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull RealFieldInfo<Double> fieldInfo =
        new RealFieldInfo<>(Double.class, "price", MAX_LENGTH);
    @NotNull RealField<Double> field = new RealField<>(fieldInfo);
    field.setFieldUpdater(fieldValueTester::setValue);
    field.setId(ID);
    return field;
  }
}