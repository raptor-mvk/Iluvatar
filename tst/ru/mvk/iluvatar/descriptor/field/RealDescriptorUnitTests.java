/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class RealDescriptorUnitTests {
	@Test
	public void constructor_ShouldSetIntegerWidth() {
		int integerWidth = 7;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(integerWidth, 5);
		int descriptorMaxWidth = realDescriptor.getIntegerWidth();
		Assert.assertEquals("constructor should set correct value of 'integerWidth'",
				integerWidth, descriptorMaxWidth);
	}

	@Test
	public void constructor_ShouldSetFractionWidth() {
		int fractionWidth = 2;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, fractionWidth);
		int descriptorFractionWidth = realDescriptor.getFractionWidth();
		Assert.assertEquals("constructor should set correct value of 'fractionWidth'",
				fractionWidth, descriptorFractionWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveIntegerWidth_ShouldThrowIluvatarRuntimeException() {
		new RealDescriptor(-3, 6);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveFractionWidth_ShouldThrowIluvatarRuntimeException() {
		new RealDescriptor(5, -2);
	}
}
