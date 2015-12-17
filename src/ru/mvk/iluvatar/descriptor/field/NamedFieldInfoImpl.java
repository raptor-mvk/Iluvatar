/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

abstract class NamedFieldInfoImpl implements NamedFieldInfo {
	@NotNull
	private final String name;

	NamedFieldInfoImpl(@NotNull String name) {
		this.name = name;
	}

	@NotNull
	@Override
	public final String getName() {
		return name;
	}

}
