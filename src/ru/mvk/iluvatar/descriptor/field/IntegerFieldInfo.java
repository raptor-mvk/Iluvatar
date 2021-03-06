/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class IntegerFieldInfo<Type extends Number> extends NaturalFieldInfo<Type> {
	public IntegerFieldInfo(@NotNull Class<Type> type, @NotNull String name, int width) {
		super(type, name, width + 1);
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.IntegerField";
	}
}