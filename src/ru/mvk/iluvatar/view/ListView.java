/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ListView<EntityType> {
  @Nullable
  Object getListView();

  @NotNull
  String getTableId();

  @NotNull
  String getAddButtonId();

  @NotNull
  String getEditButtonId();

  @NotNull
  String getRemoveButtonId();

  void refreshTable();

  void selectRowByIndex(int selectedIndex);

  void selectRowByEntity(@Nullable EntityType selectedEntity);

  void scrollToIndex(int index);

  void scrollToEntity(@Nullable EntityType entity);

  void clearSelection();

  void setAddButtonHandler(@NotNull Runnable handler);

  void setEditButtonHandler(@NotNull Runnable handler);

  void setRemoveButtonHandler(@NotNull Runnable handler);

  void setSelectedEntitySetter(@NotNull Consumer<EntityType> setter);

  void setSelectedIndexSetter(@NotNull Consumer<Integer> setter);

  void setListSupplier(@NotNull Supplier<List<EntityType>> listSupplier);

  void setTotalSupplier(@NotNull Supplier<EntityType> totalSupplier);
}
