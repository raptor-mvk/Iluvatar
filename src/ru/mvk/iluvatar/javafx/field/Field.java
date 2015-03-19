/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface Field<Type> {
  void setFieldUpdater(@NotNull Consumer<Type> fieldUpdater);

  void setFieldValue(@NotNull Object value);
}
