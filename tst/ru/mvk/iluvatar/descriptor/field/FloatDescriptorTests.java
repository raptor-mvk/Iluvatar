/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class FloatDescriptorTests {
	@Test
	public void constructor_ShouldSetMaxWidth() {
		int maxWidth = 7;
		@NotNull FloatDescriptor floatDescriptor = new FloatDescriptor(maxWidth, 5);
		int descriptorMaxWidth = floatDescriptor.getMaxWidth();
		Assert.assertEquals("constructor should set correct value of 'maxWidth'", maxWidth,
				descriptorMaxWidth);
	}

	@Test
	public void constructor_ShouldSetFractionWidth() {
		int fractionWidth = 2;
		@NotNull FloatDescriptor floatDescriptor = new FloatDescriptor(5, fractionWidth);
		int descriptorFractionWidth = floatDescriptor.getFractionWidth();
		Assert.assertEquals("constructor should set correct value of 'fractionWidth'",
				fractionWidth, descriptorFractionWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveMaxWidth_ShouldThrowIluvatarRuntimeException() {
		new FloatDescriptor(-3, 6);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveFractionWidth_ShouldThrowIluvatarRuntimeException() {
		new FloatDescriptor(5, -2);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_TooLargeFractionWidth_ShouldThrowIluvatarRuntimeException() {
		new FloatDescriptor(5, 5);
	}
}
