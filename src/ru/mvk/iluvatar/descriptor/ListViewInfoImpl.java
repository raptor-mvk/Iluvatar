/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ListViewInfoImpl<EntityType> implements ListViewInfo<EntityType> {
  @NotNull
  private final Map<String, ColumnInfo> columns;
  @NotNull
  private final Class<EntityType> entityType;
  private final boolean hasTotalRow;

  public ListViewInfoImpl(@NotNull Class<EntityType> entityType, boolean hasTotalRow) {
    this.entityType = entityType;
    this.hasTotalRow = hasTotalRow;
    columns = new LinkedHashMap<>();
  }

  @NotNull
  @Override
  public Class<EntityType> getEntityType() {
    return entityType;
  }

  @Override
  public int getColumnsCount() {
    return columns.size();
  }

  @Override
  public boolean hasTotalRow() {
    return hasTotalRow;
  }

  @NotNull
  @Override
  public Iterator<Entry<String, ColumnInfo>> getIterator() {
    return columns.entrySet().iterator();
  }

  @NotNull
  @Override
  public ColumnInfo getColumnInfo(@NotNull String columnKey) {
    @Nullable ColumnInfo result = columns.get(columnKey);
    if (result == null) {
      throw new IluvatarRuntimeException("SimpleListViewInfo: no column with key '" +
          columnKey + "'");
    }
    return result;
  }

  @Override
  public void addColumnInfo(@NotNull String columnKey, @NotNull ColumnInfo columnInfo) {
    columns.put(columnKey, columnInfo);
  }
}
