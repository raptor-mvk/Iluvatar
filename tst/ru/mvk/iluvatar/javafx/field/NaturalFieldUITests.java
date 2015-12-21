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
import ru.mvk.iluvatar.descriptor.field.NaturalFieldInfo;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

public class NaturalFieldUITests extends UITests<NaturalField<?>> {
	private static final int MAX_LENGTH = 10;
	@NotNull
	private static final String ID = "fieldId";
	@NotNull
	private final FieldValueTester<Long> fieldValueTester = new FieldValueTester<>();

	@Test
	public void input_ShouldSetValue() {
		@NotNull Long input = 876531L;
		@NotNull String inputText = Long.toString(input);
		safeClickById(ID).type(inputText);
		@Nullable Long fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input natural number should set correct value", input,
				fieldValue);
	}

	@Test
	public void emptyField_ShouldSetValueToZero() {
		@NotNull String inputText = "2398063";
		safeClickById(ID).type(inputText);
		emptyField(ID);
		@Nullable Long fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("empty field should set value to zero", Long.valueOf(0),
				fieldValue);
	}

	@Test
	public void shortNonNegativeInput_FieldShouldContainWholeText() {
		@NotNull String inputText = "2352";
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, inputText);
	}

	@Test
	public void longNonNegativeInput_FieldShouldContainTruncatedText() {
		@NotNull String inputText = "798793135498734";
		@NotNull String resultText = inputText.substring(0, MAX_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void shortMixedInput_FieldShouldContainOnlyDigits() {
		@NotNull String inputText = "2chairCutter4destiny6ParrotQuite48,HALLO";
		@NotNull String resultText = filterNatural(inputText, MAX_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void longMixedInput_FieldShouldContainOnlyDigitsTruncatedToMaxLength() {
		@NotNull String inputText = "2pineapple4halloween6primary48,try827his8173custody332";
		@NotNull String resultText = filterNatural(inputText, MAX_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void shortNegativeInput_FieldShouldContainOnlyPositivePart() {
		@NotNull String inputText = "-346";
		@NotNull String resultText = filterNatural(inputText, MAX_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void shortRealInput_FieldShouldContainOnlyDigits() {
		@NotNull String inputText = "346.35";
		@NotNull String resultText = filterNatural(inputText, MAX_LENGTH);
		safeClickById(ID).type(inputText);
		assertTextFieldByIdContainsText(ID, resultText);
	}

	@Test
	public void shortNonNegativePasteFromClipboard_FieldShouldContainWholeText() {
		@NotNull String clipboardText = "2342";
		putToClipboard(clipboardText);
		pasteFromClipboardIntoTextField(ID);
		assertTextFieldByIdContainsText(ID, clipboardText);
	}

	@Test
	public void longNonNegativePasteFromClipboard_FieldShouldContainNoText() {
		putToClipboard("8329759273957329857932");
		pasteFromClipboardIntoTextField(ID);
		assertTextFieldByIdContainsText(ID, "");
	}

	@Test
	public void shortMixedPasteFromClipboard_FieldShouldContainNoText() {
		@NotNull String clipboardText = "2sa342";
		putToClipboard(clipboardText);
		pasteFromClipboardIntoTextField(ID);
		assertTextFieldByIdContainsText(ID, "");
	}

	@Test
	public void longMixedPasteFromClipboard_FieldShouldContainNoText() {
		putToClipboard("1said63Klaus4faded655epsilonSpoon4576strategy");
		pasteFromClipboardIntoTextField(ID);
		assertTextFieldByIdContainsText(ID, "");
	}

	@Test
	public void inputIntoFilledField_ShouldNotMoveCaret() {
		@NotNull String inputText = "5468748612";
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
		@NotNull String inputText = "43632";
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
		@NotNull String inputText = "7896543210";
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
		@NotNull NaturalFieldInfo<Long> fieldInfo =
				new NaturalFieldInfo<>(Long.class, "size", MAX_LENGTH);
		@NotNull NaturalField<Long> field = new NaturalField<>(fieldInfo);
		field.setFieldUpdater(fieldValueTester::setValue);
		fieldValueTester.setValue(100L);
		field.setId(ID);
		return field;
	}
}
