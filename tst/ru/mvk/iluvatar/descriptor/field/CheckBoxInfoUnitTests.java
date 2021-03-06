/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class CheckBoxInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "enabled";
		@NotNull NamedFieldInfo checkBoxInfo = new CheckBoxInfo(name);
		@NotNull String checkBoxName = checkBoxInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				checkBoxName);
	}

	@Test
	public void getJFXFieldClassName_ShouldReturnCheckBoxField() {
		@NotNull NamedFieldInfo checkBoxInfo = new CheckBoxInfo("active");
		@NotNull String fieldName = checkBoxInfo.getJFXFieldClassName();
		Assert.assertEquals("getJFXFieldClassName() should return CheckBoxField",
				"ru.mvk.iluvatar.javafx.field.CheckBoxField", fieldName);
	}
}
