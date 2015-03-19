/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class TextFieldInfo extends SizedFieldInfoImpl {
  public TextFieldInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public String getJFXFieldClassName() {
    return "ru.mvk.iluvatar.javafx.field.LimitedTextField";
  }
}
