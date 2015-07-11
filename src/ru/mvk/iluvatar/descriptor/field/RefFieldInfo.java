/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RefFieldInfo<Type extends Serializable, RefType extends RefAble>
    extends SizedFieldInfoImpl implements ListFieldInfo<Type, RefType> {
  @NotNull
  private final ListAdapter<Type, RefType> listAdapter;

  public RefFieldInfo(@NotNull String name, int width,
                      @NotNull ListAdapter<Type, RefType> listAdapter) {
    super(name, width);
    this.listAdapter = listAdapter;
  }

  @NotNull
  @Override
  public Class<Type> getType() {
    return listAdapter.getType();
  }

  @NotNull
  @Override
  public Class<RefType> getRefType() {
    return listAdapter.getRefType();
  }

  @NotNull
  @Override
  public Supplier<List<RefType>> getListSupplier() {
    return listAdapter.getListSupplier();
  }

  @NotNull
  @Override
  public Function<Serializable, RefType> getFinder() {
    return listAdapter.getFinder();
  }

  @NotNull
  @Override
  public String getJFXFieldClassName() {
    return "ru.mvk.iluvatar.javafx.field.RefField";
  }
}
