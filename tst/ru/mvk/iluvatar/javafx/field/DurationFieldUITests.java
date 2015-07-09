/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.DurationFieldInfo;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

@Deprecated
@Ignore
public class DurationFieldUITests extends UITests<DurationField> {
  private static final int SECONDS_IN_HOUR = 3600;
  private static final int SECONDS_IN_MINUTE = 60;
  @NotNull
  private static final String ID = "fieldId";
  private static final int MAX_LENGTH = 2;
  @NotNull
  private static final String MASK = "##:##:##";
  @NotNull
  private static final String DEFAULT_VALUE = MASK.replace('#', '0');
  @NotNull
  private final FieldValueTester<Integer> fieldValueTester = new FieldValueTester<>();

  @Test
  public void focus_ShouldSelectFirstDigit() {
    @NotNull TextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    int selectionStart = field.getSelection().getStart();
    Assert.assertEquals("focus should select first digit", 0, selectionStart);
  }

  @Test
  public void focus_ShouldSelectSingleDigit() {
    @NotNull TextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    int selectionLength = field.getSelection().getLength();
    Assert.assertEquals("focus should select single digit", 1, selectionLength);
  }

  @Test
  public void focus_AfterCaretMove_ShouldPositionToFirstDigit() {
    @NotNull MaskedTextField field = safeFindById(ID);
    @NotNull String inputText = "28";
    @NotNull String expectedValue = getExpectedText(inputText);
    runAndWait(field::requestFocus);
    type(KeyCode.END);
    runAndWait(() -> {
      @Nullable Node parent = field.getParent();
      if (parent == null) {
        throw new RuntimeException("Parent is null");
      }
      parent.requestFocus();
      field.requestFocus();
    });
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("focus after caret move should move caret to first digit",
                           expectedValue, fieldValue);
  }

  @Test
  public void mouseClick_ShouldSelectSingleDigit() {
    @NotNull TextField field = safeFindById(ID);
    safeClickById(ID);
    int selectionLength = field.getSelection().getLength();
    Assert.assertEquals("mouseClick should select single digit", 1, selectionLength);
  }

  @Test
  public void input_ShouldSetValue() {
    @NotNull String inputText = "281709";
    @NotNull Integer expectedValue = restoreValue(inputText);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @Nullable Integer fieldValue = fieldValueTester.getValue();
    Assert.assertEquals("input natural number should set correct value", expectedValue,
                           fieldValue);
  }

  @Test
  public void setFieldValue_NoHours_ShouldSetCorrectValue() {
    @NotNull DurationField field = safeFindById(ID);
    @NotNull Integer expectedValue = 13 * SECONDS_IN_MINUTE + 3;
    runAndWait(() -> field.setFieldValue(expectedValue));
    @NotNull String fieldText = field.getText();
    @NotNull String fieldFilteredText = StringUtils.remove(fieldText, ':');
    @NotNull Integer value = restoreValue(fieldFilteredText);
    Assert.assertEquals("setFieldValue should set correct value, when seconds count is " +
                            "one-digit", expectedValue, value);
  }

  @Test
  public void setFieldValue_NoHoursAndMinutes_ShouldSetCorrectValue() {
    @NotNull DurationField field = safeFindById(ID);
    @NotNull Integer expectedValue = 3;
    runAndWait(() -> field.setFieldValue(expectedValue));
    @NotNull String fieldText = field.getText();
    @NotNull String fieldFilteredText = StringUtils.remove(fieldText, ':');
    @NotNull Integer value = restoreValue(fieldFilteredText);
    Assert.assertEquals("setFieldValue should set correct value, when seconds count is " +
                            "one-digit", expectedValue, value);
  }

  @Test
  public void setFieldValue_OneDigitSeconds_ShouldSetCorrectValue() {
    @NotNull DurationField field = safeFindById(ID);
    @NotNull Integer expectedValue = 10 * SECONDS_IN_HOUR + 13 * SECONDS_IN_MINUTE + 3;
    runAndWait(() -> field.setFieldValue(expectedValue));
    @NotNull String fieldText = field.getText();
    @NotNull String fieldFilteredText = StringUtils.remove(fieldText, ':');
    @NotNull Integer value = restoreValue(fieldFilteredText);
    Assert.assertEquals("setFieldValue should set correct value, when seconds count is " +
                            "one-digit", expectedValue, value);
  }

  @Test
  public void setFieldValue_OneDigitMinutes_ShouldSetCorrectValue() {
    @NotNull DurationField field = safeFindById(ID);
    @NotNull Integer expectedValue = 54 * SECONDS_IN_HOUR + 7 * SECONDS_IN_MINUTE + 23;
    runAndWait(() -> field.setFieldValue(expectedValue));
    @NotNull String fieldText = field.getText();
    @NotNull String fieldFilteredText = StringUtils.remove(fieldText, ':');
    @NotNull Integer value = restoreValue(fieldFilteredText);
    Assert.assertEquals("setFieldValue should set correct value, when minutes count is " +
                            "one-digit", expectedValue, value);
  }

  @Test
  public void setFieldValue_OneDigitHours_ShouldSetCorrectValue() {
    @NotNull DurationField field = safeFindById(ID);
    @NotNull Integer expectedValue = 3 * SECONDS_IN_HOUR + 42 * SECONDS_IN_MINUTE + 23;
    runAndWait(() -> field.setFieldValue(expectedValue));
    @NotNull String fieldText = field.getText();
    @NotNull String fieldFilteredText = StringUtils.remove(fieldText, ':');
    @NotNull Integer value = restoreValue(fieldFilteredText);
    Assert.assertEquals("setFieldValue should set correct value, when hours count is " +
                            "one-digit", expectedValue, value);
  }

  @Test
  public void incorrectFirstMinutesDigit_FieldShouldNotAccept() {
    @NotNull String inputText = "368";
    @NotNull String expectedInput = inputText.substring(0, 2);
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should not accept incorrect first 'minutes' digit",
                           expectedValue, fieldValue);
  }

  @Test
  public void incorrectFirstSecondsDigit_FieldShouldNotAccept() {
    @NotNull String inputText = "10309";
    @NotNull String expectedInput = inputText.substring(0, 4);
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should not accept incorrect first 'seconds' digit",
                           expectedValue, fieldValue);
  }

  @Test
  public void leftArrowKey_ShouldMoveCaretLeft() {
    @NotNull String inputText = "8916";
    @NotNull String inputDigit = "3";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.LEFT).type(inputDigit);
    @NotNull String expectedInput = inputText.substring(0, 3) + inputDigit;
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("right arrow key should move caret to left", expectedValue,
                           fieldValue);
  }

  @Test
  public void rightArrowKey_ShouldMoveCaretToRight() {
    @NotNull String inputText = "3819";
    @NotNull String inputDigit = "5";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.RIGHT).type(inputDigit);
    @NotNull String expectedInput = inputText + "0" + inputDigit;
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("left arrow key should move caret to right", expectedValue,
                           fieldValue);
  }

  @Test
  public void backSpaceKey_ShouldMoveCaretToLeft() {
    @NotNull String inputText = "8713";
    @NotNull String inputDigit = "5";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.BACK_SPACE).type(inputDigit);
    @NotNull String expectedInput = inputText.substring(0, 3) + inputDigit;
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("backSpace key should move caret to left", expectedValue,
                           fieldValue);
  }

  @Test
  public void backSpaceKey_ShouldReplacePreviousDigitWithZero() {
    @NotNull String inputText = "8713";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.BACK_SPACE);
    @NotNull String expectedInput = inputText.substring(0, 3);
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("backSpace key should replace previous digit with zero",
                           expectedValue, fieldValue);
  }

  @Test
  public void deleteKey_ShouldSetTextToZero() {
    @NotNull String inputText = "2333";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.DELETE);
    @NotNull String expectedValue = MASK.replace('#', '0');
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("delete should set text to zeroed mask", expectedValue,
                           fieldValue);
  }

  @Test
  public void emptyField_ShouldSetTextToZero() {
    @NotNull String inputText = "2333";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    emptyField(ID);
    @NotNull String expectedValue = MASK.replace('#', '0');
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("empty field should set text to zeroed mask", expectedValue,
                           fieldValue);
  }

  @Test
  public void deleteKey_ShouldMoveCaretToLeftmostPosition() {
    @NotNull String inputText = "8713";
    @NotNull String inputDigit = "5";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.DELETE).type(inputDigit);
    @NotNull String expectedInput = inputDigit;
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("delete key should move caret to the leftmost position",
                           expectedValue, fieldValue);
  }

  @Test
  public void homeKey_ShouldMoveCaretToLeftmostPosition() {
    @NotNull String inputText = "8713";
    @NotNull String inputDigit = "5";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.HOME).type(inputDigit);
    @NotNull String expectedInput = inputDigit + inputText.substring(1);
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("home key should move caret to the leftmost position",
                           expectedValue, fieldValue);
  }

  @Test
  public void endKey_ShouldMoveCaretToRightmostPosition() {
    @NotNull String inputText = "8713";
    @NotNull String inputDigit = "5";
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText).type(KeyCode.END).type(inputDigit);
    @NotNull String expectedInput = inputText + "0" + inputDigit;
    @NotNull String expectedValue = getExpectedText(expectedInput);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("end key should move caret to the rightmost position",
                           expectedValue, fieldValue);
  }

  @Test
  public void shortDigitalInput_FieldShouldContainWholeText() {
    @NotNull String inputText = "012345";
    @NotNull String expectedValue = getExpectedText(inputText);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should contain whole text, when input is short and " +
                            "contains only digits", expectedValue, fieldValue);
  }

  @Test
  public void longDigitalInput_FieldShouldContainTruncatedText() {
    @NotNull String inputText = "3523521435";
    @NotNull String expectedValue = getExpectedText(inputText);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should contain truncated text, when input is long and " +
                            "contains only digits", expectedValue, fieldValue);
  }

  @Test
  public void shortMixedInput_FieldShouldContainOnlyDigits() {
    @NotNull String inputText = "34glamour12trip11mask";
    @NotNull String filteredInputText = filterNatural(inputText);
    @NotNull String expectedValue = getExpectedText(filteredInputText);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should contain all digits, when input is short and mixed",
                           expectedValue, fieldValue);
  }

  @Test
  public void longMixedInput_FieldShouldContainOnlyDigitsTruncatedToMaxLength() {
    @NotNull String inputText = "341people9zap3tone78meta";
    @NotNull String filteredInputText = filterNatural(inputText);
    @NotNull String expectedValue = getExpectedText(filteredInputText);
    @NotNull MaskedTextField field = safeFindById(ID);
    runAndWait(field::requestFocus);
    type(inputText);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("field should contain digits truncated to maxLength, when " +
                            "input is long and mixed", expectedValue, fieldValue);
  }

  @Test
  public void PasteFromClipboard_ShouldNotChangeText() {
    @NotNull String clipboardText = "2342";
    putToClipboard(clipboardText);
    @NotNull MaskedTextField field = safeFindById(ID);
    @NotNull String fieldValue = field.getValue();
    Assert.assertEquals("paste from clipboard, that contains more than 1 character," +
                            "should not change text", DEFAULT_VALUE, fieldValue);
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull GridPane root = new GridPane();
    @NotNull DurationFieldInfo fieldInfo = new DurationFieldInfo("duration", MAX_LENGTH);
    @NotNull DurationField field = new DurationField(fieldInfo);
    field.setFieldUpdater(fieldValueTester::setValue);
    field.setId(ID);
    root.add(field, 0, 0);
    return root;
  }

  @NotNull
  private String getExpectedText(@NotNull String input) {
    @NotNull StringBuilder result = new StringBuilder(DEFAULT_VALUE);
    int inputLength = input.length();
    int maskLength = MASK.lastIndexOf('#');
    for (int i = 0, j = 0; i < inputLength; ) {
      if (MASK.charAt(j) == '#') {
        char digit = input.charAt(i++);
        result.setCharAt(j, digit);
      }
      j += (j < maskLength) ? 1 : 0;
    }
    return result.toString();
  }

  private int restoreValue(@NotNull String text) {
    int textLength = text.length();
    if (textLength != 6) {
      throw new RuntimeException("Incorrect text length");
    }
    @NotNull String secondsString = text.substring(4);
    @NotNull String minutesString = text.substring(2, 4);
    @NotNull String hoursString = text.substring(0, 2);
    return Integer.parseInt(hoursString) * SECONDS_IN_HOUR +
               Integer.parseInt(minutesString) * SECONDS_IN_MINUTE +
               Integer.parseInt(secondsString);
  }
}
