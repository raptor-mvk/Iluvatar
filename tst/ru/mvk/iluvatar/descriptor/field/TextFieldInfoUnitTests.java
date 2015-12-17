/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class TextFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "name";
		@NotNull TextFieldInfo textFieldInfo = new TextFieldInfo(name, 20);
		@NotNull String fieldName = textFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				fieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 10;
		@NotNull TextFieldInfo textFieldInfo = new TextFieldInfo("id", width);
		int fieldWidth = textFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				fieldWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveWidth_ShouldThrowIluvatarRuntimeException() {
		new TextFieldInfo("error", 0);
	}
}
