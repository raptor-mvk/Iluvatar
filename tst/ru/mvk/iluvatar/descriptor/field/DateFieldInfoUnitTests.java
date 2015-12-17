/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class DateFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "enabled";
		@NotNull NamedFieldInfo dateFieldInfo = new DateFieldInfo(name);
		@NotNull String dateFieldName = dateFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				dateFieldName);
	}
}
