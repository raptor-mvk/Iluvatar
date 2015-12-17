/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

abstract class SizedFieldInfoImpl extends NamedFieldInfoImpl implements SizedFieldInfo {
	private final int width;

	SizedFieldInfoImpl(@NotNull String name, int width) {
		super(name);
		if (width <= 0) {
			throw new IluvatarRuntimeException("SizedFieldInfoImpl: non-positive width");
		}
		this.width = width;
	}

	@Override
	public final int getWidth() {
		return width;
	}
}
