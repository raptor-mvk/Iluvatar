/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class RealDescriptor {
	private final int integerWidth;

	private final int fractionWidth;

	public RealDescriptor(int integerWidth, int fractionWidth) {
		if (integerWidth <= 0 || fractionWidth <= 0) {
			throw new IluvatarRuntimeException("RealDescriptor: wrong width");
		}
		this.integerWidth = integerWidth;
		this.fractionWidth = fractionWidth;
	}

	public int getIntegerWidth() {
		return integerWidth;
	}

	public int getFractionWidth() {
		return fractionWidth;
	}
}
