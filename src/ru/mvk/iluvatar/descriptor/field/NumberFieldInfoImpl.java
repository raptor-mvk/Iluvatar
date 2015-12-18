/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

abstract class NumberFieldInfoImpl<Type extends Number> extends SizedFieldInfoImpl
		implements NumberFieldInfo<Type> {
	@NotNull
	private final Class<Type> type;

	NumberFieldInfoImpl(@NotNull Class<Type> type, @NotNull String name, int width) {
		super(name, width);
		this.type = type;
	}

	@NotNull
	@Override
	public final Class<Type> getType() {
		return type;
	}
}
