/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class NaturalFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "quantity";
		@NotNull NumberFieldInfo<Byte> naturalFieldInfo =
				new NaturalFieldInfo<>(Byte.class, name, 2);
		@NotNull String fieldName = naturalFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				fieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 8;
		@NotNull NumberFieldInfo<Long> naturalFieldInfo =
				new NaturalFieldInfo<>(Long.class, "mark", width);
		int fieldWidth = naturalFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				fieldWidth);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_NonPositiveWidth_ShouldThrowIluvatarRuntimeException() {
		new NaturalFieldInfo<>(Long.class, "wrong", -7);
	}

	@Test
	public void constructor_ShouldSetType() {
		@NotNull Class<Short> type = Short.class;
		@NotNull NumberFieldInfo<Short> naturalFieldInfo =
				new NaturalFieldInfo<>(type, "level", 2);
		@NotNull Class<?> fieldType = naturalFieldInfo.getType();
		Assert.assertEquals("constructor should set correct value of 'type'", type,
				fieldType);
	}

	@Test
	public void constructor_ShouldAcceptByteType() {
		new NaturalFieldInfo<>(Byte.class, "value", 2);
	}

	@Test
	public void constructor_ShouldAcceptShortType() {
		new NaturalFieldInfo<>(Short.class, "width", 3);
	}

	@Test
	public void constructor_ShouldAcceptIntegerType() {
		new NaturalFieldInfo<>(Integer.class, "weight", 6);
	}

	@Test
	public void constructor_ShouldAcceptLongType() {
		new NaturalFieldInfo<>(Long.class, "volume", 10);
	}

	@Test
	public void constructor_ShouldAcceptFloatType() {
		new NaturalFieldInfo<>(Float.class, "cost", 5);
	}

	@Test
	public void constructor_ShouldAcceptDoubleType() {
		new NaturalFieldInfo<>(Double.class, "size", 7);
	}

	@Test(expected = IluvatarRuntimeException.class)
	public void constructor_ShouldNotAcceptObjectType() {
		new NaturalFieldInfo<>(Object.class, "height", 3);
	}
}
