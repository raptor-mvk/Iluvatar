/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.view;

import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public interface StringSupplier extends UnaryOperator<String> {
  @NotNull
  @Override
  String apply(@NotNull String stringId);
}
