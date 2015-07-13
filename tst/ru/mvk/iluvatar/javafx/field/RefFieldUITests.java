/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.ListAdapter;
import ru.mvk.iluvatar.descriptor.field.RefFieldInfo;
import ru.mvk.iluvatar.descriptor.field.TestListAdapter;
import ru.mvk.iluvatar.test.FieldValueTester;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.UITests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RefFieldUITests extends UITests<RefField<Integer, Student>> {
  private static final int MAX_LENGTH = 20;
  @NotNull
  private static final String ID = "fieldId";
  @NotNull
  private static final String LAST = "Third";
  @NotNull
  private static final String UNIQUE = "Second";
  @NotNull
  private static final String FIRST = "Fifth";
  @NotNull
  private static final String FIRST_OTHER = "Fourth";
  @NotNull
  private final FieldValueTester<Integer> fieldValueTester = new FieldValueTester<>();
  @NotNull
  private final List<Student> studentList = prepareStudents();
  @NotNull
  private final ListAdapter<Integer, Student> listAdapter =
      new TestListAdapter(studentList);
  @NotNull
  private final GridPane root = new GridPane();

  @Test
  public void shouldBeNoValuesBeforeReload() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    @NotNull List<Student> items = getRefFieldItems(field);
    Assert.assertEquals("RefField should not contain values before reload", 0,
                           items.size());
  }

  @Test
  public void shouldContainSortedSuppliedListAfterReload() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    @NotNull List<Student> items = getRefFieldItems(field);
    @NotNull List<Student> sortedStudentList = new ArrayList<>(studentList);
    Collections.sort(sortedStudentList, new RefComparator<>());
    Assert.assertEquals("RefField should contain sorted supplied list after reload",
                           sortedStudentList, items);
  }

  @Test
  public void setFieldValue_shouldSelectValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    @NotNull Student expectedItem = studentList.get(1);
    int id = expectedItem.getId();
    runAndWait(field::reload);
    runAndWait(() -> field.setFieldValue(id));
    @Nullable Student fieldSelectedItem = field.getValue();
    Assert.assertEquals("setFieldValue should select correct value", expectedItem,
                           fieldSelectedItem);
  }

  @Test
  public void selection_shouldSetValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(KeyCode.DOWN).type(KeyCode.DOWN).type(KeyCode.ENTER);
    @NotNull Student expectedItem = studentList.get(1);
    @Nullable Student fieldSelectedItem = field.getValue();
    Assert.assertEquals("selection should set correct value", expectedItem,
                           fieldSelectedItem);
  }

  @Test
  public void inputLastUniquePrefix_shouldSetLastValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(LAST.charAt(0)).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("last unique prefix input should select last value", LAST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void inputUniquePrefix_shouldSetExactValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(UNIQUE.charAt(0)).type(UNIQUE.charAt(1)).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("unique prefix input should select exact value", UNIQUE,
                           fieldSelectedItem.toString());
  }

  @Test
  public void inputAfterLastPrefix_shouldSetLastValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type('Z').type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("after last prefix input should select last value", LAST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void inputBeforeFirstPrefix_shouldSetFirstValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type('A').type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("before first prefix input should select first value", FIRST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void inputFirstUniquePrefix_shouldSetFirstValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(FIRST.charAt(0)).type(FIRST.charAt(1)).type(FIRST.charAt(2)).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("first unique prefix input should select first value", FIRST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void backspaceInput_shouldRemovesLastSymbolInPrefix() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(FIRST_OTHER.charAt(0)).type(FIRST_OTHER.charAt(1));
    type(KeyCode.BACK_SPACE).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("backSpace input should remove last symbol in prefix", FIRST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void escapeInput_shouldEmptyPrefix() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(LAST.charAt(0)).type(LAST.charAt(1)).type(KeyCode.ESCAPE).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("escape input should empty prefix", FIRST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void lostFocus_shouldNotChangeValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(LAST.charAt(0)).type(LAST.charAt(1));
    runAndWait(root::requestFocus);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("lost focus should empty prefix", LAST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void lostFocus_shouldEmptyPrefix() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type(LAST.charAt(0)).type(LAST.charAt(1));
    runAndWait(root::requestFocus);
    safeClickById(ID);
    type(FIRST.charAt(0)).type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("lost focus should empty prefix", FIRST,
                           fieldSelectedItem.toString());
  }

  @Test
  public void inputWrongPrefix_shouldSetFirstGreaterValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    safeClickById(ID);
    type('O').type(KeyCode.ENTER);
    @NotNull Student fieldSelectedItem = getRefFieldValue(field);
    Assert.assertEquals("wrong prefix input should select first greater value", UNIQUE,
                           fieldSelectedItem.toString());
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull RefFieldInfo<Integer, Student> fieldInfo =
        new RefFieldInfo<>("Student", MAX_LENGTH, listAdapter);
    @NotNull RefField<Integer, Student> field = new RefField<>(fieldInfo);
    field.setFieldUpdater(fieldValueTester::setValue);
    fieldValueTester.setValue(34);
    field.setId(ID);
    root.add(field, 0, 0);
    return root;
  }

  private List<Student> prepareStudents() {
    @NotNull List<Student> result = new ArrayList<>();
    @NotNull Student student = new Student();
    student.setId(5);
    student.setName("First");
    result.add(student);
    @NotNull Student student2 = new Student();
    student2.setId(3);
    student2.setName(UNIQUE);
    result.add(student2);
    @NotNull Student student3 = new Student();
    student3.setId(8);
    student3.setName(LAST);
    result.add(student3);
    @NotNull Student student4 = new Student();
    student4.setId(1);
    student4.setName(FIRST_OTHER);
    result.add(student4);
    @NotNull Student student5 = new Student();
    student5.setId(7);
    student5.setName(FIRST);
    result.add(student5);
    return result;
  }
}
