/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;

import java.util.Iterator;
import java.util.Map.Entry;

public interface ListViewInfo<EntityType> {
  @NotNull
  Class<EntityType> getEntityType();

  int getColumnsCount();

  boolean isTotalRow();

  void setTotalRow(boolean totalRow);

  boolean isRemoveAllowed();

  void setRemoveAllowed(boolean removeAllowed);

  @NotNull
  Iterator<Entry<String, ColumnInfo>> getIterator();

  @NotNull
  ColumnInfo getColumnInfo(@NotNull String columnKey);

  void addColumnInfo(@NotNull String columnKey, @NotNull ColumnInfo columnInfo);
}
