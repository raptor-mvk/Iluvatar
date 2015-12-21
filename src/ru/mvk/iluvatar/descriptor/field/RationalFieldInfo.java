/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class RationalFieldInfo<Type extends Number> extends NaturalFieldInfo<Type>
		implements RealFieldInfo<Type> {
	private final int integerWidth;
	private final int fractionWidth;

	public RationalFieldInfo(@NotNull Class<Type> type, @NotNull String name,
	                         @NotNull RealDescriptor realDescriptor) {
		super(type, name, realDescriptor.getIntegerWidth() +
				realDescriptor.getFractionWidth() + 2);
		integerWidth = realDescriptor.getIntegerWidth();
		fractionWidth = realDescriptor.getFractionWidth();
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.RealField";
	}

	@Override
	public int getIntegerWidth() {
		return integerWidth;
	}

	@Override
	public int getFractionWidth() {
		return fractionWidth;
	}
}
