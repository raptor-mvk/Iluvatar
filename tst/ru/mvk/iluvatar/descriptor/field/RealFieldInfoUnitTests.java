/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class RealFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "share";
		@NotNull FloatNumberFieldInfo<Double> realFieldInfo =
				new RealFieldInfo<>(Double.class, name, new FloatDescriptor(5, 2));
		@NotNull String fieldName = realFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				fieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 6;
		@NotNull FloatNumberFieldInfo<Float> realFieldInfo =
				new RealFieldInfo<>(Float.class, "percent", new FloatDescriptor(width, 2));
		int fieldWidth = realFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				fieldWidth);
	}

	@Test
	public void constructor_ShouldSetFractionWidth() {
		int fractionWidth = 2;
		@NotNull FloatNumberFieldInfo<Float> realFieldInfo =
				new RealFieldInfo<>(Float.class, "fraction",
						new FloatDescriptor(4, fractionWidth));
		int fieldWidth = realFieldInfo.getFractionWidth();
		Assert.assertEquals("constructor should set correct value of 'fractionWidth'",
				fractionWidth, fieldWidth);
	}

	@Test
	public void constructor_ShouldSetType() {
		@NotNull Class<Double> type = Double.class;
		@NotNull FloatNumberFieldInfo<Double> realFieldInfo =
				new RealFieldInfo<>(type, "price", new FloatDescriptor(6, 3));
		@NotNull Class<?> fieldType = realFieldInfo.getType();
		Assert.assertEquals("constructor should set correct value of 'type'", type,
				fieldType);
	}

	@Test
	public void constructor_ShouldAcceptFloatType() {
		new RealFieldInfo<>(Float.class, "quantity", new FloatDescriptor(5, 2));
	}

	@Test
	public void constructor_ShouldAcceptDoubleType() {
		new RealFieldInfo<>(Double.class, "price", new FloatDescriptor(10, 3));
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ShouldNotAcceptByteType() {
		new RealFieldInfo<>(Byte.class, "coefficient", new FloatDescriptor(4, 1));
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ShouldNotAcceptShortType() {
		new RealFieldInfo<>(Short.class, "percent", new FloatDescriptor(9, 5));
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ShouldNotAcceptIntegerType() {
		new RealFieldInfo<>(Integer.class, "quality", new FloatDescriptor(6, 2));
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ShouldNotAcceptLongType() {
		new RealFieldInfo<>(Long.class, "share", new FloatDescriptor(4, 1));
	}
}
