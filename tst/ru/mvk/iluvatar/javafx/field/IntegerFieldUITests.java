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
import ru.mvk.iluvatar.descriptor.field.IntegerFieldInfo;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

@Deprecated
@Ignore
public class IntegerFieldUITests extends UITests<IntegerField<?>> {
  private static final int MAX_LENGTH = 9;
  @NotNull
  private static final String ID = "fieldId";
  @NotNull
  private final FieldValueTester<Integer> fieldValueTester = new FieldValueTester<>();

  @Test
  public void input_ShouldSetValue() {
    @NotNull Integer input = 132467;
    @NotNull String inputText = Integer.toString(input);
    safeClickById(ID).type(inputText);
    @Nullable Integer fieldValue = fieldValueTester.getValue();
    Assert.assertEquals("input integer should set corresponding value", input,
        fieldValue);
  }

  @Test
  public void emptyField_SetValueToZero() {
    @NotNull String inputText = "879315";
    safeClickById(ID).type(inputText);
    emptyField(ID);
    @Nullable Integer fieldValue = fieldValueTester.getValue();
    Assert.assertEquals("empty field should set value to zero", new Integer(0),
        fieldValue);
  }

  @Test
  public void inputMinus_ShouldSetValueToZero() {
    @NotNull String inputText = "-";
    safeClickById(ID).type(inputText);
    @Nullable Integer fieldValue = fieldValueTester.getValue();
    Assert.assertEquals("input \"-\" should set value to zero", new Integer(0),
        fieldValue);
  }

  @Test
  public void shortIntegerInput_FieldShouldContainWholeText() {
    @NotNull String inputText = "-1754";
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, inputText);
  }

  @Test
  public void longIntegerInput_FieldShouldContainTruncatedText() {
    @NotNull String inputText = "457817257239879865443";
    @NotNull String resultText = inputText.substring(0, MAX_LENGTH);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortMixedInput_FieldShouldContainOnlyInteger() {
    @NotNull String inputText = "-8seedLocate1Harpooned3HeroPot-4587Algebra";
    @NotNull String resultText = filterInteger(inputText);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void longMixedInput_FieldShouldContainOnlyIntegerTruncatedToMaxLength() {
    @NotNull String inputText = "seymour-26Fuzzy356Azimuth548trinity113Tower68934KEY12";
    @NotNull String filteredText = filterInteger(inputText);
    @NotNull String resultText = filteredText.substring(0, MAX_LENGTH);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortRealInput_FieldShouldContainOnlyDigits() {
    @NotNull String inputText = "165.3733";
    @NotNull String resultText = filterInteger(inputText);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortIntegerPasteFromClipboard_FieldShouldContainWholeText() {
    @NotNull String clipboardText = "65845";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, clipboardText);
  }

  @Test
  public void longIntegerPasteFromClipboard_FieldShouldContainNoText() {
    putToClipboard("-54070257436578982184");
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void shortMixedPasteFromClipboard_FieldShouldContainNoText() {
    @NotNull String clipboardText = "854kal353";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void longMixedPasteFromClipboard_FieldShouldContainNoText() {
    putToClipboard("1hazelEyes435Skunk6Sparrow2Height44refrigerator");
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void inputIntoFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "-89267458";
    safeClickById(ID).type(inputText);
    type("1");
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    int expectedCaretPosition = inputText.length();
    Assert.assertEquals("input into filled field should not move caret",
        expectedCaretPosition, caretPosition);
  }

  @Test
  public void inputWrongCharacterInsideText_ShouldNotMoveCaret() {
    @NotNull String inputText = "2363";
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
    @NotNull String inputText = "468127563";
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
    @NotNull IntegerFieldInfo<Integer> fieldInfo =
        new IntegerFieldInfo<>(Integer.class, "quantity", MAX_LENGTH);
    @NotNull IntegerField<Integer> field = new IntegerField<>(fieldInfo);
    field.setFieldUpdater(fieldValueTester::setValue);
    field.setId(ID);
    return field;
  }
}
