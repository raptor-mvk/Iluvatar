/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class NaturalFieldInfo<Type extends Number> extends NumberFieldInfoImpl<Type> {
	public NaturalFieldInfo(@NotNull Class<Type> type, @NotNull String name,
	                        int width) {
		super(type, name, width);
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.NaturalField";
	}
}
