/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Deprecated
@Ignore
public class DurationFieldInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "duration";
    @NotNull DurationFieldInfo durationFieldInfo = new DurationFieldInfo(name, 5);
    @NotNull String fieldName = durationFieldInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        fieldName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 6;
    @NotNull DurationFieldInfo durationFieldInfo = new DurationFieldInfo("length", width);
    int fieldWidth = durationFieldInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        fieldWidth);
  }
}
