/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

@Deprecated
public class DurationFieldInfo extends SizedFieldInfoImpl {
  // width is number of digits in "hour" part
  public DurationFieldInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public String getJFXFieldClassName() {
    return "ru.mvk.iluvatar.javafx.field.DurationField";
  }
}
