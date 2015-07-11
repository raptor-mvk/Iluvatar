/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.SizedFieldInfo;

public class LimitedTextField extends SizedTextField<String> {

  public LimitedTextField(@NotNull SizedFieldInfo fieldInfo) {
    super(fieldInfo.getWidth(), String.class);
  }

  @Override
  protected boolean check(@NotNull String value) {
    return value.length() <= getMaxLength();
  }

  @Override
  protected String convertValue(@NotNull String value) {
    return value;
  }
}
