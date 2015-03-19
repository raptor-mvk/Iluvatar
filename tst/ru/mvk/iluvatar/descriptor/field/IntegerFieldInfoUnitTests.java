/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class IntegerFieldInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "width";
    @NotNull NumberFieldInfo<Short> integerFieldInfo =
        new IntegerFieldInfo<>(Short.class, name, 10);
    @NotNull String fieldName = integerFieldInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        fieldName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 5;
    @NotNull NumberFieldInfo<Long> integerFieldInfo =
        new IntegerFieldInfo<>(Long.class, "value", width);
    int fieldWidth = integerFieldInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        fieldWidth);
  }

  @Test
  public void constructor_ShouldSetType() {
    @NotNull Class<Integer> type = Integer.class;
    @NotNull NumberFieldInfo<Integer> integerFieldInfo =
        new IntegerFieldInfo<>(type, "width", 4);
    @NotNull Class<?> fieldType = integerFieldInfo.getType();
    Assert.assertEquals("constructor should set correct value of 'type'", type,
        fieldType);
  }
}
