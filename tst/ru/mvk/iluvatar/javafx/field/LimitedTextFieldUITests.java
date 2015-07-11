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
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.TextFieldInfo;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

public class LimitedTextFieldUITests extends UITests<LimitedTextField> {
  @NotNull
  private static final String ID = "fieldId";
  private static final int MAX_LENGTH = 12;
  @NotNull
  private final FieldValueTester<String> fieldValueTester = new FieldValueTester<>();

  @Test
  public void input_ShouldSetValue() {
    @NotNull String inputText = "Test";
    safeClickById(ID).type(inputText);
    @Nullable String fieldValue = fieldValueTester.getValue();
    Assert.assertEquals("input into field should set correct value", inputText,
                           fieldValue);
  }

  @Test
  public void shortInput_FieldShouldContainWholeText() {
    @NotNull String inputText = "Test";
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, inputText);
  }

  @Test
  public void longInput_FieldShouldContainTruncatedText() {
    @NotNull String inputText = "It is a very long test";
    @NotNull String resultText = inputText.substring(0, MAX_LENGTH);
    safeClickById(ID).type(inputText);
    assertTextFieldByIdContainsText(ID, resultText);
  }

  @Test
  public void shortPasteFromClipboard_FieldShouldContainWholeText() {
    @NotNull String clipboardText = "Paste";
    putToClipboard(clipboardText);
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, clipboardText);
  }

  @Test
  public void longPasteFromClipboard_FieldShouldContainNoText() {
    putToClipboard("Very long paste from clipboard");
    pasteFromClipboardIntoTextField(ID);
    assertTextFieldByIdContainsText(ID, "");
  }

  @Test
  public void inputIntoFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "Obliteration";
    safeClickById(ID).type(inputText);
    type("a");
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    int expectedCaretPosition = inputText.length();
    Assert.assertEquals("input into filled field should not move caret",
                           expectedCaretPosition, caretPosition);
  }

  @Test
  public void inputInsideFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "Refrigerator";
    safeClickById(ID).type(inputText);
    type(KeyCode.LEFT);
    type("a");
    @NotNull TextField field = safeFindById(ID);
    int caretPosition = field.getCaretPosition();
    int expectedCaretPosition = inputText.length() - 1;
    Assert.assertEquals("input inside filled field should not move caret",
                           expectedCaretPosition, caretPosition);
  }

  @Test
  public void pasteFromClipboardIntoFilledField_ShouldNotMoveCaret() {
    @NotNull String inputText = "Triangulation";
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
    @NotNull TextFieldInfo fieldInfo = new TextFieldInfo("Text", MAX_LENGTH);
    @NotNull LimitedTextField field = new LimitedTextField(fieldInfo);
    field.setFieldUpdater(fieldValueTester::setValue);
    field.setId(ID);
    return field;
  }
}