/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

public interface RealFieldInfo<Type extends Number> extends NumberFieldInfo<Type> {
	int getIntegerWidth();

	int getFractionWidth();
}
