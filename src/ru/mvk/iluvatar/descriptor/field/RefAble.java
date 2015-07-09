/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface RefAble {
  @NotNull
  Serializable getId();

  @NotNull
  String getName();
}
