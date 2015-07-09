/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class RefFieldInfo<Type> extends SizedFieldInfoImpl
    implements ListFieldInfo<Type> {
  @NotNull
  private final Supplier<List<Type>> listSupplier;

  RefFieldInfo(@NotNull String name, int width,
               @NotNull Supplier<List<Type>> listSupplier) {
    super(name, width);
    this.listSupplier = listSupplier;
  }

  @NotNull
  @Override
  public Supplier<List<Type>> getListSupplier() {
    return listSupplier;
  }

  @NotNull
  @Override
  public String getJFXFieldClassName() {
    return "ru.mvk.iluvatar.javafx.field.RefField";
  }
}
