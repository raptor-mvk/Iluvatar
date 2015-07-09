/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.NumberFieldInfo;

@Deprecated
public class IntegerField<Type> extends NaturalField<Type> {
  public IntegerField(@NotNull NumberFieldInfo<Type> fieldInfo) {
    super(fieldInfo);
  }

  @Override
  protected boolean check(@NotNull String value) {
    return false;
  }
}