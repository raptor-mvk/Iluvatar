/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public interface Dao<EntityType, PrimaryKeyType extends Serializable> {
  @NotNull
  PrimaryKeyType create(@NotNull EntityType entity);

  @Nullable
  EntityType read(@NotNull PrimaryKeyType id);

  void update(@NotNull EntityType entity);

  void delete(@NotNull EntityType entity);

  @NotNull
  List<EntityType> list();

  @NotNull
  List<EntityType> orderedList(@NotNull String field, boolean isAscending);

  @NotNull
  Class<EntityType> getEntityType();
}