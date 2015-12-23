/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.ViewInfo;
import ru.mvk.iluvatar.descriptor.ViewInfoImpl;
import ru.mvk.iluvatar.descriptor.field.*;
import ru.mvk.iluvatar.javafx.field.*;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.UITests;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.StringSupplier;
import ru.mvk.iluvatar.view.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class JFXViewUITests extends UITests<View<Student>> {
	@NotNull
	private final String datePattern = "dd.MM.yyyy";
	@NotNull
	private final DateTimeFormatter dateFormatter =
			DateTimeFormatter.ofPattern(datePattern);
	@NotNull
	private final IdGenerator idGenerator = new JFXIdGenerator(Student.class);
	@NotNull
	private final RealDescriptor realDescriptor = new RealDescriptor(2, 2);
	@NotNull
	private final FieldValueTester<Boolean> saveButtonState = new FieldValueTester<>();
	@NotNull
	private final FieldValueTester<Boolean> cancelButtonState = new FieldValueTester<>();
	@NotNull
	private final Consumer<Boolean> saveButtonHandler = saveButtonState::setValue;
	@NotNull
	private final Runnable cancelButtonHandler = () -> cancelButtonState.setValue(true);
	@NotNull
	private final List<Student> studentList = prepareStudents();
	@NotNull
	private final ViewInfo<Student> viewInfo = prepareViewInfo();
	@NotNull
	private final Student student = prepareStudent();
	@NotNull
	private final StringSupplier stringSupplier = StringUtils::reverse;
	@NotNull
	private final String saveButtonName = "Save";
	@NotNull
	private final String cancelButtonName = "Cancel";

	@Test
	public void fieldLabelsShouldBeCorrect() {
		@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
		assertFieldLabelsAreCorrect(idGenerator, iterator, stringSupplier);
	}

	@Test
	public void fieldValuesShouldBeCorrect() {
		@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
		assertFieldsValues(idGenerator, iterator, student);
	}

	@Test
	public void fieldsShouldBeOfCorrectTypes() {
		@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
		@NotNull Map<String, Class<? extends Node>> nodeTypes = new HashMap<>();
		nodeTypes.put("id", NaturalField.class);
		nodeTypes.put("name", LimitedTextField.class);
		nodeTypes.put("gpa", RealField.class);
		nodeTypes.put("penalty", IntegerField.class);
		nodeTypes.put("graduated", CheckBoxField.class);
		nodeTypes.put("neighbour", RefField.class);
		nodeTypes.put("enrollmentDate", DateField.class);
		assertFieldsHaveCorrectTypes(idGenerator, iterator, nodeTypes);
	}

	@Test
	public void neighbourField_ShouldContainCorrectList() {
		@NotNull String fieldId = idGenerator.getFieldId("neighbour");
		@NotNull RefField<Integer, Student> field = safeFindById(fieldId);
		@NotNull List<Student> fieldItems = getRefFieldItems(field);
		Assert.assertEquals("neighbour field should contain correct value list",
				studentList, fieldItems);
	}

	@Test
	public void nodesTabOrderShouldBeCorrect() {
		@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
		assertNodesOrder(idGenerator, iterator);
	}

	@Test
	public void saveButton_ShouldHaveCorrectCaption() {
		@NotNull String saveButtonId = idGenerator.getButtonId(saveButtonName);
		@NotNull Button saveButton = safeFindById(saveButtonId);
		@NotNull String expectedCaption = stringSupplier.apply(saveButtonName);
		@NotNull String saveButtonCaption = saveButton.getText();
		Assert.assertEquals("Save button should have correct caption", expectedCaption,
				saveButtonCaption);
	}

	@Test
	public void cancelButton_ShouldHaveCorrectCaption() {
		@NotNull String cancelButtonId = idGenerator.getButtonId(cancelButtonName);
		@NotNull Button cancelButton = safeFindById(cancelButtonId);
		@NotNull String expectedCaption = stringSupplier.apply(cancelButtonName);
		@NotNull String cancelButtonCaption = cancelButton.getText();
		Assert.assertEquals("Cancel button should have correct caption", expectedCaption,
				cancelButtonCaption);
	}

	@Test
	public void clickSave_ShouldCallSaveButtonHandlerWithFalseParameter() {
		@NotNull String saveButtonId = idGenerator.getButtonId(saveButtonName);
		saveButtonState.setValue(true);
		safeClickById(saveButtonId);
		@Nullable Boolean saveButtonWasClicked = saveButtonState.getValue();
		Assert.assertEquals("click Save button should execute saveButtonHandler with " +
				"false parameter", false, saveButtonWasClicked);
	}

	@Test
	public void clickCancel_ShouldCallCancelButtonHandler() {
		@NotNull String cancelButtonId = idGenerator.getButtonId(cancelButtonName);
		cancelButtonState.setValue(false);
		safeClickById(cancelButtonId);
		@Nullable Boolean cancelButtonWasClicked = cancelButtonState.getValue();
		Assert.assertEquals("Click Cancel button should execute cancelButtonHandler", true,
				cancelButtonWasClicked);
	}

	@Test
	public void inputIntoIdField_ShouldSetIdValue() {
		@NotNull String fieldId = idGenerator.getFieldId("id");
		int idValue = 302;
		@NotNull String idValueString = Integer.toString(idValue);
		emptyField(fieldId);
		safeClickById(fieldId).type(idValueString);
		int studentIdValue = student.getId();
		Assert.assertEquals("input into 'id' field should set value of 'id'", idValue,
				studentIdValue);
	}

	@Test
	public void inputIntoNameField_ShouldSetNameValue() {
		@NotNull String fieldId = idGenerator.getFieldId("name");
		@NotNull String nameValue = "John Doe";
		emptyField(fieldId);
		safeClickById(fieldId).type(nameValue);
		@NotNull String studentNameValue = student.getName();
		Assert.assertEquals("input into 'name' field should set value of 'name'", nameValue,
				studentNameValue);
	}

	@Test
	public void inputIntoGpaField_ShouldSetGpaValue() {
		@NotNull String fieldId = idGenerator.getFieldId("gpa");
		short gpaValue = 320;
		int multiplier = (int) Math.pow(10.0, realDescriptor.getFractionWidth());
		@Nullable String gpaValueString = Double.toString((double) gpaValue / multiplier);
		if (gpaValueString == null) {
			throw new RuntimeException("gpaValueString is null");
		}
		emptyField(fieldId);
		safeClickById(fieldId).type(gpaValueString);
		short studentGpaValue = student.getGpa();
		Assert.assertEquals("input into 'gpa' field should set value of 'gpa'",
				gpaValue, studentGpaValue);
	}

	@Test
	public void inputIntoPenaltyField_ShouldSetPenaltyValue() {
		@NotNull String fieldId = idGenerator.getFieldId("penalty");
		short penaltyValue = -68;
		@NotNull String penaltyValueString = Integer.toString(penaltyValue);
		emptyField(fieldId);
		safeClickById(fieldId).type(penaltyValueString);
		int studentPenaltyValue = student.getPenalty();
		Assert.assertEquals("input into 'penalty' field should set value of 'penalty'",
				penaltyValue, studentPenaltyValue);
	}

	@Test
	public void inputIntoEnrollmentDateField_ShouldSetEnrollmentDateValue() {
		@NotNull String fieldId = idGenerator.getFieldId("enrollmentDate");
		LocalDate enrollmentDateValue = LocalDate.of(2005, 12, 31);
		@NotNull String enrollmentDateString = enrollmentDateValue.format(dateFormatter);
		emptyField(fieldId);
		safeClickById(fieldId).type(enrollmentDateString);
		@NotNull String saveButtonId = idGenerator.getButtonId(saveButtonName);
		@NotNull Button saveButton = safeFindById(saveButtonId);
		runAndWait(saveButton::requestFocus);
		@NotNull LocalDate studentEnrollmentDateValue = student.getEnrollmentDate();
		Assert.assertEquals("input into 'enrollmentDate' field should set value of " +
				"'enrollmentDate'", enrollmentDateValue, studentEnrollmentDateValue);
	}

	@Test
	public void clickOntoGraduatedField_ShouldSetGraduatedValue() {
		@NotNull String fieldId = idGenerator.getFieldId("graduated");
		boolean graduatedValue = !student.isGraduated();
		safeClickById(fieldId);
		boolean studentGraduatedValue = student.isGraduated();
		Assert.assertEquals("input into 'id' field should set value of 'id'", graduatedValue,
				studentGraduatedValue);
	}

	@Test
	public void selectionInNeighbourField_ShouldSetNeighbourValue() {
		@NotNull String fieldId = idGenerator.getFieldId("neighbour");
		safeClickById(fieldId);
		type(KeyCode.UP).type(KeyCode.ENTER);
		int expectedNeighbourValue = studentList.get(1).getId();
		int studentNeighbourValue = student.getNeighbour();
		Assert.assertEquals("selection into 'neighbour' field should set value of " +
				"'neighbour'", expectedNeighbourValue, studentNeighbourValue);
	}

	@Test
	public void enterKey_ShouldCallSaveButtonHandlerWithFalseParameter() {
		saveButtonState.setValue(true);
		push(KeyCode.ENTER);
		@Nullable Boolean saveButtonWasClicked = saveButtonState.getValue();
		Assert.assertEquals("enter key should execute saveButtonHandler with " +
				"false parameter", false, saveButtonWasClicked);
	}

	@Test
	public void enterKeyLastEditDateField_ShouldSaveDateFieldValue() {
		@NotNull String fieldId = idGenerator.getFieldId("enrollmentDate");
		LocalDate enrollmentDateValue = LocalDate.of(2010, 2, 28);
		@NotNull String enrollmentDateString = enrollmentDateValue.format(dateFormatter);
		emptyField(fieldId);
		safeClickById(fieldId).type(enrollmentDateString);
		push(KeyCode.ENTER);
		@NotNull LocalDate studentEnrollmentDateValue = student.getEnrollmentDate();
		Assert.assertEquals("enter key after last edit in DateField should save " +
				"DateField value", enrollmentDateValue, studentEnrollmentDateValue);
	}

	@Test
	public void escapeKey_ShouldCallCancelButtonHandler() {
		cancelButtonState.setValue(false);
		push(KeyCode.ESCAPE);
		@Nullable Boolean cancelButtonWasClicked = cancelButtonState.getValue();
		Assert.assertEquals("escape key button should execute cancelButtonHandler", true,
				cancelButtonWasClicked);
	}

	@NotNull
	@Override
	protected Parent getRootNode() {
		@NotNull JFXView<Student> view = new JFXView<>(viewInfo, stringSupplier, idGenerator);
		setObjectUnderTest(view);
		view.setSaveButtonHandler(saveButtonHandler);
		view.setCancelButtonHandler(cancelButtonHandler);
		Parent result = view.getView(student, false);
		return (result != null) ? result : new GridPane();
	}

	@NotNull
	private ViewInfo<Student> prepareViewInfo() {
		@NotNull ViewInfo<Student> result = new ViewInfoImpl<>(Student.class);
		result.addFieldInfo("id", new NaturalFieldInfo<>(Integer.class, "id", 10));
		result.addFieldInfo("name", new TextFieldInfo("name", 100));
		result.addFieldInfo("gpa", new RationalFieldInfo<>(Short.class, "gpa",
				realDescriptor));
		result.addFieldInfo("penalty", new IntegerFieldInfo<>(Short.class, "penalty", 5));
		result.addFieldInfo("graduated", new CheckBoxInfo("graduated"));
		@NotNull ListAdapter<Integer, Student> listAdapter = new TestListAdapter(studentList);
		result.addFieldInfo("neighbour", new RefFieldInfo<>("neighbour", 20, listAdapter));
		@NotNull TemporalDescriptor<LocalDate> temporalDescriptor =
				new TemporalDescriptor<>(LocalDate.of(2000, 1, 1), datePattern);
		result.addFieldInfo("enrollmentDate", new DateFieldInfo("enrollmentDate",
				temporalDescriptor));
		return result;
	}

	@NotNull
	private Student prepareStudent() {
		@NotNull Student result = new Student();
		result.setId(1);
		result.setName("Robert Stark");
		result.setGpa((short) 375);
		result.setPenalty((short) -2);
		result.setGraduated(false);
		result.setLecturesTime(113560);
		result.setNeighbour(2);
		result.setEnrollmentDate(LocalDate.of(2013, 2, 5));
		return result;
	}

	private List<Student> prepareStudents() {
		@NotNull List<Student> result = new ArrayList<>();
		@NotNull Student student = new Student();
		student.setId(3);
		student.setName("Jim Wealth");
		result.add(student);
		@NotNull Student student2 = new Student();
		student2.setId(5);
		student2.setName("Tony Peterson");
		result.add(student2);
		@NotNull Student student3 = new Student();
		student3.setId(2);
		student3.setName("Victor Somebody");
		result.add(student3);
		return result;
	}
}
