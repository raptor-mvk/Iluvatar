/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.view.IdGenerator;

/* TODO: refine tests */
public class JFXIdGeneratorUnitTests {
	@Test
	public void getTableId_ShouldReturnClassNameList() {
		@NotNull IdGenerator idGenerator = new JFXIdGenerator(Student.class);
		@NotNull String expectedValue = "Student-list";
		@NotNull String value = idGenerator.getTableId();
		Assert.assertEquals("getTableId should return className-list", expectedValue,
				value);
	}

	@Test
	public void getButtonId_ShouldReturnClassNameButtonNameButton() {
		@NotNull IdGenerator idGenerator = new JFXIdGenerator(Student.class);
		@NotNull String expectedValue = "Student-Save-button";
		@NotNull String value = idGenerator.getButtonId("Save");
		Assert.assertEquals("getTableId should return className-buttonName-button",
				expectedValue, value);
	}

	@Test
	public void getFieldId_ShouldReturnClassNameFieldKeyField() {
		@NotNull IdGenerator idGenerator = new JFXIdGenerator(Student.class);
		@NotNull String expectedValue = "Student-Name-field";
		@NotNull String value = idGenerator.getFieldId("Name");
		Assert.assertEquals("getTableId should return className-fieldKey-field",
				expectedValue, value);
	}

	@Test
	public void getLabelId_ShouldReturnClassnameLabelKeyLabel() {
		@NotNull IdGenerator idGenerator = new JFXIdGenerator(Student.class);
		@NotNull String expectedValue = "Student-Name-label";
		@NotNull String value = idGenerator.getLabelId("Name");
		Assert.assertEquals("getTableId should return className-labelKey-id", expectedValue,
				value);
	}

}
