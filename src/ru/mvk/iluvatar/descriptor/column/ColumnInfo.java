/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import org.jetbrains.annotations.NotNull;

public interface ColumnInfo {
  @NotNull
  String getName();

  int getWidth();

  @NotNull
  ViewFormatter getViewFormatter();
}
