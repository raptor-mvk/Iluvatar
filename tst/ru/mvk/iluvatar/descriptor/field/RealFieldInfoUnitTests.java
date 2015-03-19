/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class RealFieldInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "share";
    @NotNull NumberFieldInfo<Double> realFieldInfo =
        new RealFieldInfo<>(Double.class, name, 5);
    @NotNull String fieldName = realFieldInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        fieldName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 6;
    @NotNull NumberFieldInfo<Float> realFieldInfo =
        new RealFieldInfo<>(Float.class, "percent", width);
    int fieldWidth = realFieldInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        fieldWidth);
  }

  @Test
  public void constructor_ShouldSetType() {
    @NotNull Class<Double> type = Double.class;
    @NotNull NumberFieldInfo<Double> realFieldInfo =
        new RealFieldInfo<>(type, "price", 6);
    @NotNull Class<?> fieldType = realFieldInfo.getType();
    Assert.assertEquals("constructor should set correct value of 'type'", type,
        fieldType);
  }

  @Test
  public void constructor_ShouldAcceptFloatType() {
    new RealFieldInfo<>(Float.class, "quantity", 5);
  }

  @Test
  public void constructor_ShouldAcceptDoubleType() {
    new RealFieldInfo<>(Double.class, "price", 10);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ShouldNotAcceptByteType() {
    new RealFieldInfo<>(Byte.class, "coefficient", 4);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ShouldNotAcceptShortType() {
    new RealFieldInfo<>(Short.class, "percent", 5);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ShouldNotAcceptIntegerType() {
    new RealFieldInfo<>(Integer.class, "quality", 6);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void constructor_ShouldNotAcceptLongType() {
    new RealFieldInfo<>(Long.class, "share", 4);
  }
}
