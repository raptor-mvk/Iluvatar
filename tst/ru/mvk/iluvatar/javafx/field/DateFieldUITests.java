/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.DateFieldInfo;
import ru.mvk.iluvatar.descriptor.field.TemporalDescriptor;
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
	private final LocalDate defaultDate = LocalDate.of(2000, 1, 1);
	@NotNull
	private final String datePattern = "dd.MM.yy";

	@Test
	public void input_ShouldSetFieldValue() {
		@NotNull String inputText = "15.02.12";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		@NotNull DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
		Assert.assertEquals("input date should set corresponding field value",
				LocalDate.parse(inputText, dateFormatter), fieldValue);
	}

	@Test
	public void input_ShouldSetFieldText() {
		@NotNull String inputText = "15.02.12";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		@Nullable String fieldText = ((DateField) safeFindById(ID)).getEditor().getText();
		Assert.assertEquals("input date should set corresponding field value",
				inputText, fieldText);
	}

	@Test
	public void wrongInput_ShouldSetFieldValueToDefaultDate() {
		@NotNull String inputText = "12.05.02";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		type("43.25.12");
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("wrong input should set field value to default date", defaultDate,
				fieldValue);
	}

	@Test
	public void wrongInput_ShouldSetValueToDefaultDate() {
		@NotNull String inputText = "07.08.11";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		type("12.25.43");
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = ((DatePicker) safeFindById(ID)).getValue();
		Assert.assertEquals("wrong input should set value to default date", defaultDate,
				fieldValue);
	}

	@Test
	public void emptyField_ShouldSetFieldValueToDefaultDate() {
		@NotNull String inputText = "25.01.14";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = fieldValueTester.getValue();
		Assert.assertEquals("empty field should set field value to default date", defaultDate,
				fieldValue);
	}

	@Test
	public void emptyField_ShouldSetValueToDefaultDate() {
		@NotNull String inputText = "27.02.00";
		safeClickById(ID).type(inputText);
		runAndWait(root::requestFocus);
		emptyField(ID);
		runAndWait(root::requestFocus);
		@Nullable LocalDate fieldValue = ((DatePicker) safeFindById(ID)).getValue();
		Assert.assertEquals("empty field should set value to default date", defaultDate,
				fieldValue);
	}

	@NotNull
	@Override
	protected Parent getRootNode() {
		root = new VBox();
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(defaultDate, datePattern);
		@NotNull DateFieldInfo dateFieldInfo = new DateFieldInfo("date", temporalDescriptor);
		@NotNull DateField field = new DateField(dateFieldInfo);
		field.setFieldUpdater(fieldValueTester::setValue);
		field.setId(ID);
		root.getChildren().addAll(field);
		return root;
	}
}
