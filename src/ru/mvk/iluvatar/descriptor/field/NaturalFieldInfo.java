/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class NaturalFieldInfo<Type> extends NumberFieldInfoImpl<Type> {
	public NaturalFieldInfo(@NotNull Class<Type> type, @NotNull String name,
	                        int width) {
		super(type, name, width);
	}

	@Override
	boolean isTypeCorrect(@NotNull Class<Type> type) {
		return type.equals(Float.class) || type.equals(Double.class) ||
				type.equals(Byte.class) || type.equals(Short.class) ||
				type.equals(Integer.class) || type.equals(Long.class);
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.NaturalField";
	}
}
