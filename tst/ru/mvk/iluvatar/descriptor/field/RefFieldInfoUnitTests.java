/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.PowerMockUtils;

import java.util.ArrayList;
import java.util.List;

public class RefFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "author";
		@NotNull List<Student> studentList = new ArrayList<>();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo<Integer, Student> refFieldInfo =
				new RefFieldInfo<>(name, 40, testListAdapter);
		@NotNull String fieldName = refFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				fieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 5;
		@NotNull List<Student> studentList = new ArrayList<>();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo refFieldInfo =
				new RefFieldInfo<>("parent", width, testListAdapter);
		int fieldWidth = refFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				fieldWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveWidth_ShouldThrowIluvatarRuntimeException() {
		@NotNull TestListAdapter testListAdapter = PowerMockUtils.mock(TestListAdapter.class);
		new RefFieldInfo<>("catch", -1, testListAdapter);
	}

	@Test
	public void constructor_ShouldSetType() {
		@NotNull List<Student> studentList = new ArrayList<>();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo refFieldInfo = new RefFieldInfo<>("root", 10, testListAdapter);
		Class<?> type = refFieldInfo.getType();
		Assert.assertEquals("constructor should set correct value of 'type'", Integer.class,
				type);
	}

	@Test
	public void constructor_ShouldSetRefType() {
		@NotNull List<Student> studentList = new ArrayList<>();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo refFieldInfo =
				new RefFieldInfo<>("window", 15, testListAdapter);
		Class<?> type = refFieldInfo.getRefType();
		Assert.assertEquals("constructor should set correct value of 'refType'",
				Student.class, type);
	}

	@Test
	public void constructor_ShouldSetListSupplier() {
		@NotNull ArrayList<Student> expectedList = new ArrayList<>();
		expectedList.add(new Student());
		expectedList.add(new Student());
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(expectedList);
		@NotNull ListFieldInfo<Integer, Student> refFieldInfo =
				new RefFieldInfo<>("label", 20, testListAdapter);
		@NotNull List<Student> refFieldList = refFieldInfo.getListSupplier().get();
		Assert.assertEquals("constructor should set correct value of 'listSupplier'",
				expectedList, refFieldList);
	}

	@Test
	public void constructor_ShouldSetFinder() {
		@NotNull List<Student> studentList = prepareStudents();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo<Integer, Student> refFieldInfo =
				new RefFieldInfo<>("label", 20, testListAdapter);
		@NotNull Student expectedStudent = studentList.get(1);
		int studentId = expectedStudent.getId();
		@NotNull Student foundStudent = refFieldInfo.getFinder().apply(studentId);
		Assert.assertEquals("constructor should set correct value of 'finder'",
				expectedStudent, foundStudent);
	}

	@Test
	public void getJFXFieldClassName_ShouldReturnRefField() {
		@NotNull List<Student> studentList = prepareStudents();
		@NotNull TestListAdapter testListAdapter = new TestListAdapter(studentList);
		@NotNull ListFieldInfo<Integer, Student> refFieldInfo =
				new RefFieldInfo<>("friend", 20, testListAdapter);
		@NotNull String fieldName = refFieldInfo.getJFXFieldClassName();
		Assert.assertEquals("getJFXFieldClassName() should return RefField",
				"ru.mvk.iluvatar.javafx.field.RefField", fieldName);
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
