/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class RationalFieldInfo<Type extends Number> extends NaturalFieldInfo<Type>
		implements RealFieldInfo<Type> {
	private final int fractionWidth;

	public RationalFieldInfo(@NotNull Class<Type> type, @NotNull String name,
	                         @NotNull RealDescriptor realDescriptor) {
		super(type, name, realDescriptor.getMaxWidth());
		fractionWidth = realDescriptor.getFractionWidth();
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
