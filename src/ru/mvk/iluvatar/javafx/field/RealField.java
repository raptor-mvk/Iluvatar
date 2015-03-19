/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.NumberFieldInfo;

public class RealField<Type> extends NaturalField<Type> {
  public RealField(@NotNull NumberFieldInfo<Type> fieldInfo) {
    super(fieldInfo);
  }
}
