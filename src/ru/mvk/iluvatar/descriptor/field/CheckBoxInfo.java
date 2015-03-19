/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class CheckBoxInfo extends NamedFieldInfoImpl {
  public CheckBoxInfo(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public String getJFXFieldClassName() {
    return "ru.mvk.iluvatar.javafx.field.CheckBoxField";
  }
}