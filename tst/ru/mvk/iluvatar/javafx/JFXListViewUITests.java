/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.ListViewInfoImpl;
import ru.mvk.iluvatar.descriptor.column.*;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.UITests;
import ru.mvk.iluvatar.view.ListView;
import ru.mvk.iluvatar.view.StringSupplier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JFXListViewUITests extends UITests<ListView<Student>> {
	@NotNull
	private final FieldValueTester<Boolean> addButtonState = new FieldValueTester<>();
	@NotNull
	private final FieldValueTester<Boolean> editButtonState = new FieldValueTester<>();
	@NotNull
	private final FieldValueTester<Boolean> removeButtonState = new FieldValueTester<>();
	@NotNull
	private final FieldValueTester<Student> selectedStudentState = new FieldValueTester<>();
	@NotNull
	private final FieldValueTester<Integer> selectedIndexState = new FieldValueTester<>();
	@NotNull
	private final Runnable addButtonHandler = () -> addButtonState.setValue(true);
	@NotNull
	private final Runnable editButtonHandler = () -> editButtonState.setValue(true);
	@NotNull
	private final Runnable removeButtonHandler = () -> removeButtonState.setValue(true);
	@NotNull
	private final Consumer<Student> selectedStudentSetter = selectedStudentState::setValue;
	@NotNull
	private final Consumer<Integer> selectedIndexSetter = selectedIndexState::setValue;
	@NotNull
	private final ListViewInfo<Student> listViewInfo = prepareListViewInfo();
	@NotNull
	private final List<Student> students = prepareStudents();
	@NotNull
	private final Supplier<List<Student>> listSupplier = () -> students;
	@NotNull
	private final StringSupplier stringSupplier = StringUtils::swapCase;
	@NotNull
	private final Student totalStudent = prepareTotalStudent();

	@Test
	public void tableShouldHaveCorrectNumberOfColumns() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String tableId = listView.getTableId();
		@NotNull TableView<Student> tableView = safeFindById(tableId);
		int listViewInfoColumnsCount = listViewInfo.getColumnsCount();
		@Nullable ObservableList<TableColumn<Student, ?>> tableColumns =
				tableView.getColumns();
		if (tableColumns == null) {
			throw new RuntimeException("TableView.getColumns() returned null");
		}
		int tableColumnsCount = tableColumns.size();
		Assert.assertEquals("Table should have correct number of columns",
				listViewInfoColumnsCount, tableColumnsCount);
	}

	@Test
	public void columnLabelsShouldBeCorrect() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String tableId = listView.getTableId();
		@NotNull TableView<Student> tableView = safeFindById(tableId);
		@NotNull Iterator<Entry<String, ColumnInfo>> iterator = listViewInfo.getIterator();
		assertColumnLabelsAreCorrect(tableView, iterator, stringSupplier);
	}


	@Test
	public void tableShouldContainStudentsData() {
		@NotNull ObservableList<Student> tableStudents = getTableViewItems();
		for (int i = 0, count = students.size(); i < count; i++) {
			@Nullable Student student = students.get(i);
			@Nullable Student tableStudent = tableStudents.get(i);
			Assert.assertEquals("Row " + i + "should contain data from " + i + "-th element " +
					"of 'students'", student, tableStudent);
		}
	}

	@Test
	public void clearSelection_ShouldSelectNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(listView::clearSelection);
		@Nullable Student selectedStudent = getSelectedItem(listView);
		Assert.assertNull("clearSelection() should select nothing", selectedStudent);
	}

	@Test
	public void clearSelection_ShouldCallSelectedItemSetterNullParameter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(new Student());
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(listView::clearSelection);
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertNull("clearSelection() should call selectedItemSetter with null " +
				"parameter", selectedStudent);
	}

	@Test
	public void clearSelection_ShouldCallSelectedIndexSetterNegativeIndex() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(100);
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(listView::clearSelection);
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("clearSelection() should call selectedItemSetter with negative " +
				"index", new Integer(-1), selectedRow);
	}

	@Test
	public void selectRowByIndex_Positive_ShouldSelectCorrectRow() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		int rowToSelect = 1;
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByIndex(rowToSelect));
		int selectedRow = getSelectedIndex(listView);
		Assert.assertEquals("selectRowByIndex(i) should select i-th row, when i is " +
				"non-negative and table has more rows, than i", rowToSelect, selectedRow);
	}

	@Test
	public void selectRowByIndex_Positive_ShouldCallSelectedItemSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(null);
		int rowToSelect = 1;
		@Nullable Student studentToSelect = students.get(rowToSelect);
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByIndex(rowToSelect));
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertEquals("selectRowByIndex(i) should call selectedItemSetter for i-th " +
						"student, when i is non-negative and table has more rows, than i",
				studentToSelect, selectedStudent);
	}

	@Test
	public void selectRowByIndex_Positive_ShouldCallSelectedIndexSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(-1);
		int rowToSelect = 1;
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByIndex(rowToSelect));
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("selectRowByIndex(i) should call selectedIndexSetter for i-th " +
						"student, when i is non-negative and table has more rows, than i",
				(Integer) rowToSelect, selectedRow);
	}

	@Test
	public void selectRowByIndex_Negative_ShouldSelectNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(-1));
		@Nullable Student selectedStudent = getSelectedItem(listView);
		Assert.assertNull("selectRowByIndex(i) should select nothing, when i is negative",
				selectedStudent);
	}

	@Test
	public void selectRowByIndex_Negative_ShouldCallSelectedItemSetterNullParameter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(new Student());
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(-1));
		runAndWait(listView::clearSelection);
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertNull("selectRowByIndex(i) should call selectedItemSetter with null " +
				"parameter, when i is negative", selectedStudent);
	}

	@Test
	public void selectRowByIndex_Negative_ShouldCallSelectedIndexSetterNegativeIndex() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(200);
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(-3));
		runAndWait(listView::clearSelection);
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("selectRowByIndex(i) should call selectedItemSetter with " +
				"negative index, when i is negative", new Integer(-1), selectedRow);
	}

	@Test
	public void selectRowByIndex_TooLarge_ShouldSelectNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(100));
		@Nullable Student selectedStudent = getSelectedItem(listView);
		Assert.assertNull("selectRowByIndex(i) should select nothing, when i is greater, " +
				"than the number of rows in the table", selectedStudent);
	}

	@Test
	public void selectRowByIndex_TooLarge_ShouldCallSelectedItemSetterNullParameter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(new Student());
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(200));
		runAndWait(listView::clearSelection);
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertNull("selectRowByIndex(i) should call selectedItemSetter with null " +
						"parameter, when i is greater, than the number of rows in the table",
				selectedStudent);
	}

	@Test
	public void selectRowByIndex_TooLarge_ShouldCallSelectedIndexSetterNegativeIndex() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(300);
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByIndex(300));
		runAndWait(listView::clearSelection);
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("selectRowByIndex(i) should call selectedItemSetter with " +
						"negative index, when i is greater, than the number of rows in the table",
				new Integer(-1), selectedRow);
	}


	@Test
	public void selectRowByEntity_CorrectEntity_ShouldSelectCorrectRow() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		int rowToSelect = 1;
		@NotNull Student entityToSelect = students.get(1);
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByEntity(entityToSelect));
		int selectedRow = getSelectedIndex(listView);
		Assert.assertEquals("selectRowByEntity(entity) should select corresponding row, " +
				"when entity belongs to list", rowToSelect, selectedRow);
	}

	@Test
	public void selectRowByEntity_CorrectEntity_ShouldCallSelectedItemSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(null);
		@Nullable Student studentToSelect = students.get(1);
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByEntity(studentToSelect));
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertEquals("selectRowByEntity(entity) should call selectedItemSetter for " +
						"corresponding entity, when entity belongs to list",
				studentToSelect, selectedStudent);
	}

	@Test
	public void selectRowByEntity_CorrectEntity_ShouldCallSelectedIndexSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(-1);
		int rowToSelect = 1;
		@NotNull Student studentToSelect = students.get(rowToSelect);
		runAndWait(listView::clearSelection);
		runAndWait(() -> listView.selectRowByEntity(studentToSelect));
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("selectRowByEntity(entity) should call selectedIndexSetter for " +
						"corresponding entity, when entity belongs to list", (Integer) rowToSelect,
				selectedRow);
	}


	@Test
	public void selectRowByEntity_WrongEntity_ShouldSelectNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull Student studentToSelect = new Student();
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByEntity(studentToSelect));
		@Nullable Student selectedStudent = getSelectedItem(listView);
		Assert.assertNull("selectRowByEntity(entity) should select nothing, when entity " +
				"does not belong to list", selectedStudent);
	}

	@Test
	public void selectRowByEntity_WrongEntity_ShouldCallSelectedItemSetterNullParameter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(new Student());
		@NotNull Student studentToSelect = new Student();
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByEntity(studentToSelect));
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertNull("selectRowByEntity(entity) should call selectedItemSetter with " +
				"null parameter, when entity does not belong to list", selectedStudent);
	}

	@Test
	public void selectRowByEntity_WrongEntity_ShouldCallSelectedIndexSetterNegativeIndex() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull Student studentToSelect = new Student();
		selectedIndexState.setValue(200);
		runAndWait(() -> listView.selectRowByIndex(0));
		runAndWait(() -> listView.selectRowByEntity(studentToSelect));
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("selectRowByEntity(entity) should call selectedItemSetter with " +
						"negative index, when entity does not belong to list", new Integer(-1),
				selectedRow);
	}

	@Test
	public void moveSelection_ShouldCallSelectedItemSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedStudentState.setValue(null);
		@Nullable Student studentToSelect = students.get(1);
		runAndWait(listView::clearSelection);
		@NotNull String tableId = listView.getTableId();
		safeClickById(tableId);
		push(KeyCode.DOWN);
		push(KeyCode.DOWN);
		@Nullable Student selectedStudent = selectedStudentState.getValue();
		Assert.assertEquals("move selection should call selectedItemSetter", studentToSelect,
				selectedStudent);
	}

	@Test
	public void moveSelection_ShouldCallSelectedIndexSetter() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		selectedIndexState.setValue(-1);
		@NotNull Integer rowToSelect = 1;
		runAndWait(listView::clearSelection);
		@NotNull String tableId = listView.getTableId();
		safeClickById(tableId);
		push(KeyCode.DOWN);
		push(KeyCode.DOWN);
		@Nullable Integer selectedRow = selectedIndexState.getValue();
		Assert.assertEquals("move selection should call selectedItemSetter", rowToSelect,
				selectedRow);
	}

	@Test
	public void addButton_ShouldHaveCorrectCaption() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String addButtonId = listView.getAddButtonId();
		@NotNull Button addButton = safeFindById(addButtonId);
		@NotNull String expectedCaption = stringSupplier.apply("Add");
		@NotNull String addButtonCaption = addButton.getText();
		Assert.assertEquals("Add button should have correct caption", expectedCaption,
				addButtonCaption);
	}

	@Test
	public void editButton_ShouldHaveCorrectCaption() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String editButtonId = listView.getEditButtonId();
		@NotNull Button editButton = safeFindById(editButtonId);
		@NotNull String expectedCaption = stringSupplier.apply("Edit");
		@NotNull String editButtonCaption = editButton.getText();
		Assert.assertEquals("Edit button should have correct caption", expectedCaption,
				editButtonCaption);
	}

	@Test
	public void removeButton_ShouldHaveCorrectCaption() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String removeButtonId = listView.getRemoveButtonId();
		@NotNull Button removeButton = safeFindById(removeButtonId);
		@NotNull String expectedCaption = stringSupplier.apply("Remove");
		@NotNull String removeButtonCaption = removeButton.getText();
		Assert.assertEquals("Remove button should have correct caption", expectedCaption,
				removeButtonCaption);
	}

	@Test
	public void clickAdd_ShouldCallAddButtonHandler() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		addButtonState.setValue(false);
		@NotNull String addButtonId = listView.getAddButtonId();
		safeClickById(addButtonId);
		@Nullable Boolean addButtonWasClicked = addButtonState.getValue();
		Assert.assertEquals("click Add button should execute addButtonHandler", true,
				addButtonWasClicked);
	}

	@Test
	public void clickEdit_ShouldCallEditButtonHandler() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		editButtonState.setValue(false);
		runAndWait(() -> listView.selectRowByIndex(1));
		@NotNull String editButtonId = listView.getEditButtonId();
		safeClickById(editButtonId);
		@Nullable Boolean editButtonWasClicked = editButtonState.getValue();
		Assert.assertEquals("click Edit button should execute editButtonHandler", true,
				editButtonWasClicked);
	}

	@Test
	public void clickRemove_ShouldCallRemoveButtonHandler() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		removeButtonState.setValue(false);
		runAndWait(() -> listView.selectRowByIndex(1));
		@NotNull String removeButtonId = listView.getRemoveButtonId();
		safeClickById(removeButtonId);
		@Nullable Boolean removeButtonWasClicked = removeButtonState.getValue();
		Assert.assertEquals("click Remove button should execute removeButtonHandler", true,
				removeButtonWasClicked);
	}

	@Test
	public void noSelection_EditButtonShouldBeDisabled() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(listView::clearSelection);
		@NotNull String editButtonId = listView.getEditButtonId();
		@NotNull Button editButton = safeFindById(editButtonId);
		boolean editButtonIsDisabled = editButton.isDisabled();
		Assert.assertTrue("Edit button should be disabled in case of no selection",
				editButtonIsDisabled);
	}

	@Test
	public void noSelection_RemoveButtonShouldBeDisabled() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(listView::clearSelection);
		@NotNull String removeButtonId = listView.getRemoveButtonId();
		@NotNull Button removeButton = safeFindById(removeButtonId);
		boolean removeButtonIsDisabled = removeButton.isDisabled();
		Assert.assertTrue("Remove button should be disabled in case of no selection",
				removeButtonIsDisabled);
	}

	@Test
	public void insertKey_ShouldCallAddButtonHandler() {
		addButtonState.setValue(false);
		push(KeyCode.INSERT);
		@Nullable Boolean addButtonWasClicked = addButtonState.getValue();
		Assert.assertEquals("insert key button should execute addButtonHandler", true,
				addButtonWasClicked);
	}

	@Test
	public void enterKey_ShouldCallEditButtonHandler() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		editButtonState.setValue(false);
		runAndWait(() -> listView.selectRowByIndex(1));
		push(KeyCode.ENTER);
		@Nullable Boolean editButtonWasClicked = editButtonState.getValue();
		Assert.assertEquals("enter key button should execute editButtonHandler", true,
				editButtonWasClicked);
	}

	@Test
	public void deleteKey_ShouldCallRemoveButtonHandler() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		removeButtonState.setValue(false);
		runAndWait(() -> listView.selectRowByIndex(1));
		push(KeyCode.DELETE);
		@Nullable Boolean removeButtonWasClicked = removeButtonState.getValue();
		Assert.assertEquals("delete key button should execute removeButtonHandler", true,
				removeButtonWasClicked);
	}

	@Test
	public void enterKey_noSelection_ShouldDoNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(listView::clearSelection);
		editButtonState.setValue(false);
		push(KeyCode.ENTER);
		@Nullable Boolean editButtonWasClicked = editButtonState.getValue();
		Assert.assertEquals("enter key button should do nothing, when there is no selection",
				false, editButtonWasClicked);
	}

	@Test
	public void deleteKey_noSelection_ShouldDoNothing() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(listView::clearSelection);
		removeButtonState.setValue(false);
		push(KeyCode.DELETE);
		@Nullable Boolean removeButtonWasClicked = removeButtonState.getValue();
		Assert.assertEquals("delete key button should do nothing, when there is no selection",
				false, removeButtonWasClicked);
	}

	@Test
	public void totalRowShouldContainTotalSupplierResult() {
		@Nullable List<Student> students = getTableViewItems();
		int totalRowId = students.size() - 1;
		Assert.assertEquals("Total row should contain total supplier result", totalStudent,
				students.get(totalRowId));
	}

	@Test
	public void totalRowSelection_EditButtonShouldBeDisabled() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(() -> listView.selectRowByEntity(totalStudent));
		@NotNull String editButtonId = listView.getEditButtonId();
		@NotNull Button editButton = safeFindById(editButtonId);
		boolean editButtonIsDisabled = editButton.isDisabled();
		Assert.assertTrue("Edit button should be disabled in case of no selection",
				editButtonIsDisabled);
	}

	@Test
	public void totalRowSelection_RemoveButtonShouldBeDisabled() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		runAndWait(() -> listView.selectRowByEntity(totalStudent));
		@NotNull String removeButtonId = listView.getRemoveButtonId();
		@NotNull Button removeButton = safeFindById(removeButtonId);
		boolean removeButtonIsDisabled = removeButton.isDisabled();
		Assert.assertTrue("Remove button should be disabled in case of no selection",
				removeButtonIsDisabled);
	}

	@NotNull
	@Override
	protected Parent getRootNode() {
		@NotNull JFXListView<Student> listView = (JFXListView<Student>) prepareListView();
		setObjectUnderTest(listView);
		@Nullable Parent result = listView.getListView();
		return (result == null) ? new GridPane() : result;
	}

	@NotNull
	private ObservableList<Student> getTableViewItems() {
		@NotNull ListView<Student> listView = getObjectUnderTest();
		@NotNull String tableId = listView.getTableId();
		@NotNull TableView<Student> tableView = safeFindById(tableId);
		@Nullable ObservableList<Student> result = tableView.getItems();
		if (result == null) {
			throw new RuntimeException("TableView.getItems() returned null");
		}
		return result;
	}

	@Nullable
	private Student getSelectedItem(@NotNull ListView<Student> listView) {
		@NotNull TableViewSelectionModel<Student> tableViewSelectionModel =
				getSelectionModel(listView);
		return tableViewSelectionModel.getSelectedItem();
	}

	private int getSelectedIndex(@NotNull ListView<Student> listView) {
		@NotNull TableViewSelectionModel<Student> tableViewSelectionModel =
				getSelectionModel(listView);
		return tableViewSelectionModel.getSelectedIndex();
	}

	@NotNull
	private TableViewSelectionModel<Student> getSelectionModel(@NotNull
	                                                           ListView<Student> listView) {
		@NotNull String tableId = listView.getTableId();
		@NotNull TableView<Student> tableView = safeFindById(tableId);
		@Nullable TableViewSelectionModel<Student> tableViewSelectionModel =
				tableView.getSelectionModel();
		if (tableViewSelectionModel == null) {
			throw new RuntimeException("TableView selectionModel is null");
		}
		return tableViewSelectionModel;
	}

	@NotNull
	private ListView<Student> prepareListView() {
		ListView<Student> result = new JFXListView<>(listViewInfo, stringSupplier);
		result.setAddButtonHandler(addButtonHandler);
		result.setEditButtonHandler(editButtonHandler);
		result.setRemoveButtonHandler(removeButtonHandler);
		result.setSelectedEntitySetter(selectedStudentSetter);
		result.setSelectedIndexSetter(selectedIndexSetter);
		result.setListSupplier(listSupplier);
		result.setTotalSupplier(() -> totalStudent);
		return result;
	}

	@NotNull
	private List<Student> prepareStudents() {
		@NotNull ArrayList<Student> result = new ArrayList<>();
		@NotNull Student student = prepareStudent();
		@NotNull Student anotherStudent = prepareAnotherStudent();
		result.add(student);
		result.add(anotherStudent);
		return result;
	}

	@NotNull
	private ListViewInfo<Student> prepareListViewInfo() {
		@NotNull ListViewInfo<Student> result = new ListViewInfoImpl<>(Student.class);
		result.showTotalRow();
		result.addColumnInfo("id", new PlainColumnInfo("id", 10));
		result.addColumnInfo("name", new TextColumnInfo("name", 50));
		result.addColumnInfo("gpa", new PlainColumnInfo("gpa", 5));
		result.addColumnInfo("penalty", new PlainColumnInfo("penalty", 5));
		result.addColumnInfo("graduated", new BooleanColumnInfo("graduated", 3));
		result.addColumnInfo("fileSize", new FileSizeColumnInfo("fileSize", 10));
		result.addColumnInfo("lecturesTime", new DurationColumnInfo("lecturesTime", 8));
		return result;
	}

	@NotNull
	private Student prepareStudent() {
		@NotNull Student result = new Student();
		result.setId(5);
		result.setName("Peter Trustworthy");
		result.setGpa(4.99);
		result.setPenalty((short) -8);
		result.setGraduated(false);
		result.setFileSize(3499549L);
		result.setLecturesTime(2330460);
		return result;
	}

	@NotNull
	private Student prepareAnotherStudent() {
		@NotNull Student result = new Student();
		result.setId(3);
		result.setName("Michael Grasshopper");
		result.setGpa(4.3);
		result.setPenalty((short) -30);
		result.setGraduated(true);
		result.setFileSize(436003L);
		result.setLecturesTime(343400);
		return result;
	}

	@NotNull
	private Student prepareTotalStudent() {
		@NotNull Student result = new Student();
		result.setId(324);
		result.setName("Total");
		result.setGpa(1.0);
		result.setPenalty((short) 0);
		result.setGraduated(true);
		result.setFileSize(0L);
		result.setLecturesTime(0);
		return result;
	}
}
