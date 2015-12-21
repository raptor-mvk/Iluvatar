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
import ru.mvk.iluvatar.descriptor.field.RationalFieldInfo;
import ru.mvk.iluvatar.descriptor.field.RealDescriptor;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

public class RealFieldUITests extends UITests<RealField<?>> {
	private static final int INTEGER_LENGTH = 6;
	private static final int FRACTION_LENGTH = 2;
	private static final int MULTIPLIER = (int) Math.pow(10.0, FRACTION_LENGTH);
	@NotNull
	private static final String ID = "fieldId";
	@NotNull
	private final FieldValueTester<Integer> fieldValueTester = new FieldValueTester<>();

	@Test
	public void input_ShouldSetValue() {
		@NotNull Integer input = -82436;
		@Nullable String inputText = Double.toString((double) input / MULTIPLIER);
		if (inputText == null) {
			throw new RuntimeException("Input text is null");
		}
		safeClickById(ID).type(inputText);
		@Nullable Integer fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input real number should set correct value",
				input, fieldValue);
	}

	@Test
	public void inputMinus_ShouldSetValueToZero() {
		@NotNull String inputText = "-";
		safeClickById(ID).type(inputText);
		@Nullable Integer fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input \"-\" should set value to zero", Integer.valueOf(0),
				fieldValue);
	}

	@Test
	public void inputPoint_ShouldSetValueToZero() {
		@NotNull String inputText = ".";
		safeClickById(ID).type(inputText);
		@Nullable Integer fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input \".\" should set value to zero", Integer.valueOf(0),
				fieldValue);
	}

	@Test
	public void inputMinusPoint_ShouldSetValueToZero() {
		@NotNull String inputText = "-.";
		safeClickById(ID).type(inputText);
		@Nullable Integer fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input \"-.\" should set value to zero", Integer.valueOf(0),
				fieldValue);
	}

	@Test
	public void emptyField_SetValueToZero() {
		@NotNull String inputText = "2352.15";
		safeClickById(ID).type(inputText);
		emptyField(ID);
		@Nullable Integer fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("empty field should set value to zero", Integer.valueOf(0),
				fieldValue);
	}

	@Test
	public void shortRealInput_FieldShouldContainWholeText() {
		@NotNull String inputText = "-532.45";
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, inputText);
	}

	@Test
	public void longIntegerInput_FieldShouldContainTruncatedIntegerText() {
		@NotNull String inputText = "45781725739879865443";
		@NotNull String expectedResultText = filterInteger(inputText, INTEGER_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, expectedResultText);
	}

	@Test
	public void realLongIntegerInput_FieldShouldContainTruncatedText() {
		@NotNull String inputText = "545523523563.2";
		@NotNull String expectedResultText =
				filterReal(inputText, INTEGER_LENGTH, FRACTION_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, expectedResultText);
	}

	@Test
	public void realLongFractionInput_FieldShouldContainTruncatedText() {
		@NotNull String inputText = "-347.8322";
		@NotNull String expectedResultText =
				filterReal(inputText, INTEGER_LENGTH, FRACTION_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, expectedResultText);
	}

	@Test
	public void shortMixedInput_FieldShouldContainOnlyNumber() {
		@NotNull String inputText = "323trainer.3validation7";
		@NotNull String resultText = filterReal(inputText, INTEGER_LENGTH, FRACTION_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void longMixedIntegerInput_FieldShouldContainOnlyTruncatedInteger() {
		@NotNull String inputText = "-pixel3612Negotiation3543Tribune352LABOR23gravity";
		@NotNull String resultText = filterReal(inputText, INTEGER_LENGTH, FRACTION_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void longMixedRealInput_FieldShouldContainOnlyTruncatedIntegerWithFraction() {
		@NotNull String inputText = "-pixel3612Negotiation3543Tribune35.2LABOR23gravity";
		@NotNull String resultText = filterReal(inputText, INTEGER_LENGTH, FRACTION_LENGTH);
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
		@NotNull String clipboardText = "234523692963";
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
		@NotNull String inputText = "43506.98";
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
		@NotNull String inputText = "-3436.45";
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
		@NotNull RationalFieldInfo<Integer> fieldInfo =
				new RationalFieldInfo<>(Integer.class, "price",
						new RealDescriptor(INTEGER_LENGTH, FRACTION_LENGTH));
		@NotNull RealField<Integer> field = new RealField<>(fieldInfo);
		field.setFieldUpdater(fieldValueTester::setValue);
		field.setId(ID);
		return field;
	}
}