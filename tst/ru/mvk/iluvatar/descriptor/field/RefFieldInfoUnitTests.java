/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.test.Student;

import java.util.ArrayList;
import java.util.List;

public class RefFieldInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "author";
    @NotNull ListFieldInfo<Integer, Student> refFieldInfo =
        new RefFieldInfo<>(name, 40, new TestListAdapter(new ArrayList<>()));
    @NotNull String fieldName = refFieldInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
                           fieldName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 5;
    @NotNull ListFieldInfo refFieldInfo =
        new RefFieldInfo<>("parent", width, new TestListAdapter(new ArrayList<>()));
    int fieldWidth = refFieldInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
                           fieldWidth);
  }

  @Test
  public void constructor_ShouldSetType() {
    @NotNull ListFieldInfo refFieldInfo =
        new RefFieldInfo<>("root", 10, new TestListAdapter(new ArrayList<>()));
    Class<?> type = refFieldInfo.getType();
    Assert.assertEquals("constructor should set correct value of 'type'", Integer.class,
                           type);
  }

  @Test
  public void constructor_ShouldSetRefType() {
    @NotNull ListFieldInfo refFieldInfo =
        new RefFieldInfo<>("window", 15, new TestListAdapter(new ArrayList<>()));
    Class<?> type = refFieldInfo.getRefType();
    Assert.assertEquals("constructor should set correct value of 'refType'",
                           Student.class, type);
  }

  @Test
  public void constructor_ShouldSetListSupplier() {
    @NotNull ArrayList<Student> expectedList = new ArrayList<>();
    expectedList.add(new Student());
    expectedList.add(new Student());
    @NotNull ListFieldInfo<Integer, Student> refFieldInfo =
        new RefFieldInfo<>("label", 20, new TestListAdapter(expectedList));
    @NotNull List<Student> refFieldList = refFieldInfo.getListSupplier().get();
    Assert.assertEquals("constructor should set correct value of 'listSupplier'",
                           expectedList, refFieldList);
  }

  @Test
  public void constructor_ShouldSetFinder() {
    @NotNull List<Student> studentList = prepareStudents();
    @NotNull ListFieldInfo<Integer, Student> refFieldInfo =
        new RefFieldInfo<>("label", 20, new TestListAdapter(studentList));
    @NotNull Student expectedStudent = studentList.get(1);
    int studentId = expectedStudent.getId();
    @NotNull Student foundStudent = refFieldInfo.getFinder().apply(studentId);
    Assert.assertEquals("constructor should set correct value of 'finder'",
                           expectedStudent, foundStudent);
  }

  @NotNull
  private List<Student> prepareStudents() {
    @NotNull List<Student> result = new ArrayList<>();
    @NotNull Student student = new Student();
    student.setId(1);
    student.setName("Mike");
    result.add(student);
    student.setId(2);
    student.setName("Fred");
    result.add(student);
    student.setId(5);
    student.setName("George");
    result.add(student);
    return result;
  }
}
