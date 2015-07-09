/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public interface ListFieldInfo<Type> extends SizedFieldInfo {
  @NotNull
  Supplier<List<Type>> getListSupplier();
}
