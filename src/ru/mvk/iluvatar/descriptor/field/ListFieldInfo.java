/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ListFieldInfo<Type extends Serializable, RefType extends RefAble>
		extends SizedFieldInfo {
	@NotNull
	Class<Type> getType();

	@NotNull
	Class<RefType> getRefType();

	@NotNull
	Supplier<List<RefType>> getListSupplier();

	@NotNull
	Function<Serializable, RefType> getFinder();
}
