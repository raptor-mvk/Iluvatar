/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class RationalFieldInfoUnitTests {
	@Test
	public void constructor_ShouldSetName() {
		@NotNull String name = "share";
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, 2);
		@NotNull RealFieldInfo<Double> realFieldInfo =
				new RationalFieldInfo<>(Double.class, name, realDescriptor);
		@NotNull String fieldName = realFieldInfo.getName();
		Assert.assertEquals("constructor should set correct value of 'name'", name,
				fieldName);
	}

	@Test
	public void constructor_ShouldSetWidth() {
		int width = 6;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(width, 2);
		@NotNull RealFieldInfo<Float> realFieldInfo =
				new RationalFieldInfo<>(Float.class, "percent", realDescriptor);
		int fieldWidth = realFieldInfo.getWidth();
		Assert.assertEquals("constructor should set correct value of 'width'", width,
				fieldWidth);
	}

	@Test
	public void constructor_ShouldSetFractionWidth() {
		int fractionWidth = 2;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(4, fractionWidth);
		@NotNull RealFieldInfo<Float> realFieldInfo =
				new RationalFieldInfo<>(Float.class, "fraction", realDescriptor);
		int fieldWidth = realFieldInfo.getFractionWidth();
		Assert.assertEquals("constructor should set correct value of 'fractionWidth'",
				fractionWidth, fieldWidth);
	}

	@Test
	public void constructor_ShouldSetType() {
		@NotNull Class<Double> type = Double.class;
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(6, 3);
		@NotNull RealFieldInfo<Double> realFieldInfo =
				new RationalFieldInfo<>(type, "price", realDescriptor);
		@NotNull Class<?> fieldType = realFieldInfo.getType();
		Assert.assertEquals("constructor should set correct value of 'type'", type,
				fieldType);
	}

	@Test
	public void getJFXFieldClassName_ShouldReturnRealField() {
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(5, 2);
		@NotNull RealFieldInfo<Short> rationalFieldInfo =
				new RationalFieldInfo<>(Short.class, "sum", realDescriptor);
		@NotNull String fieldName = rationalFieldInfo.getJFXFieldClassName();
		Assert.assertEquals("getJFXFieldClassName() should return RealField",
				"ru.mvk.iluvatar.javafx.field.RealField", fieldName);
	}
}
