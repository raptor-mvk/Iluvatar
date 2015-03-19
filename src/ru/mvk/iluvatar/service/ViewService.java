/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.service;

import org.jetbrains.annotations.NotNull;

public interface ViewService<EntityType> {
  void showView(boolean isNewEntity);

  EntityType getNewEntity();

  void showListView();

  void removeEntity();

  @NotNull
  Class<EntityType> getEntityType();

  void setDefaultOrder(@NotNull String fieldKey, boolean isAscending);
}

