/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class RealFieldInfo<Type> extends NaturalFieldInfo<Type>
		implements FloatNumberFieldInfo<Type> {
	private final int fractionWidth;

	public RealFieldInfo(@NotNull Class<Type> type, @NotNull String name,
	                     @NotNull FloatDescriptor floatDescriptor) {
		super(type, name, floatDescriptor.getMaxWidth());
		fractionWidth = floatDescriptor.getFractionWidth();
	}

	@Override
	boolean isTypeCorrect(@NotNull Class<Type> type) {
		return type.equals(Float.class) || type.equals(Double.class);
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.RealField";
	}

	@Override
	public int getFractionWidth() {
		return fractionWidth;
	}
}
