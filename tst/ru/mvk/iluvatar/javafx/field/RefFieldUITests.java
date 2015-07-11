/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
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
import java.util.List;

public class RefFieldUITests extends UITests<RefField<Integer, Student>> {
  private static final int MAX_LENGTH = 20;
  @NotNull
  private static final String ID = "fieldId";
  @NotNull
  private final FieldValueTester<Integer> fieldValueTester = new FieldValueTester<>();
  @NotNull
  private final List<Student> studentList = prepareStudents();
  @NotNull
  private final ListAdapter<Integer, Student> listAdapter =
      new TestListAdapter(studentList);

  @Test
  public void shouldBeNoValuesBeforeReload() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    @NotNull List<Student> items = getRefFieldItems(field);
    Assert.assertEquals("RefField should not contain values before reload", 0,
                           items.size());
  }

  @Test
  public void shouldContainSuppliedListAfterReload() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    runAndWait(field::reload);
    @NotNull List<Student> items = getRefFieldItems(field);
    Assert.assertEquals("RefField should contain supplied list after reload", studentList,
                           items);
  }

  @Test
  public void setFieldValue_shouldSelectValue() {
    @NotNull RefField<Integer, Student> field = safeFindById(ID);
    @NotNull Student expectedItem = studentList.get(1);
    int id = expectedItem.getId();
    System.out.println("before");
    runAndWait(field::reload);
    runAndWait(() -> field.setFieldValue(id));
    System.out.println("after");
    @Nullable Student fieldSelectedItem = getRefFieldSelected(field);
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
    @Nullable Student fieldSelectedItem = getRefFieldSelected(field);
    Assert.assertEquals("selection should set correct value", expectedItem,
                           fieldSelectedItem);
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
    return field;
  }

  private List<Student> prepareStudents() {
    @NotNull List<Student> result = new ArrayList<>();
    @NotNull Student student = new Student();
    student.setId(5);
    student.setName("First");
    result.add(student);
    student.setId(3);
    student.setName("Second");
    result.add(student);
    student.setId(8);
    student.setName("Third");
    result.add(student);
    return result;
  }
}
