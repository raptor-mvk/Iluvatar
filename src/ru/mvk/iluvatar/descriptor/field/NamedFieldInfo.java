/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public interface NamedFieldInfo {
  @NotNull
  String getName();

  @NotNull
  String getJFXFieldClassName();
}
