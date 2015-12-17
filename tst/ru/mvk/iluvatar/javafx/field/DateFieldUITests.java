/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.utils.UITests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFieldUITests extends UITests<DateField> {
	@NotNull
	private static VBox root = new VBox();
	@NotNull
	private static final String ID = "fieldId";
	@NotNull
	private final FieldValueTester<LocalDate> fieldValueTester = new FieldValueTester<>();
	@NotNull
	private final DateTimeFormatter dateFormatter =
			DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Test
	public void input_ShouldSetFieldValue() {
		@NotNull String inputText = "12.02.2015";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("input date should set corresponding field value",
				LocalDate.parse(inputText, dateFormatter), fieldValue);
	}

	@Test
	public void wrongInput_ShouldSetFieldValueToDefaultDate() {
		@NotNull String inputText = "12.05.2002";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		type("43.25.12");
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("wrong input should set field value to default date",
				DateField.getDefaultDate(), fieldValue);
	}

	@Test
	public void wrongInput_ShouldSetValueToDefaultDate() {
		@NotNull String inputText = "12.05.2002";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		type("43.25.12");
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = ((DatePicker)safeFindById(ID)).getValue();
		Assert.assertEquals("wrong input should set value to default date",
				DateField.getDefaultDate(), fieldValue);
	}

	@Test
	public void emptyField_ShouldSetFieldValueToDefaultDate() {
		@NotNull String inputText = "12.05.2002";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("empty field should set field value to default date",
				DateField.getDefaultDate(), fieldValue);
	}

	@Test
	public void emptyField_ShouldSetValueToDefaultDate() {
		@NotNull String inputText = "12.05.2002";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = ((DatePicker)safeFindById(ID)).getValue();
		Assert.assertEquals("empty field should set value to default date",
				DateField.getDefaultDate(), fieldValue);
	}

	@Test
	public void setDefaultValue_SetsDefaultDateValue() {
		@NotNull LocalDate defaultDate = LocalDate.of(2013, 2, 7);
		DateField.setDefaultDate(defaultDate);
		emptyField(ID);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("setDefaultValue() should set defaultDate value",
				defaultDate, fieldValue);
	}

	@NotNull
	@Override
	protected Parent getRootNode() {
		root = new VBox();
		@NotNull DateField field = new DateField();
		field.setFieldUpdater(fieldValueTester::setValue);
		field.setId(ID);
		root.getChildren().addAll(field);
		return root;
	}
}
