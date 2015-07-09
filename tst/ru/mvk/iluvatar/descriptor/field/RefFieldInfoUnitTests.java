/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RefFieldInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "author";
    @NotNull RefFieldInfo<String> refFieldInfo =
        new RefFieldInfo<>(name, 40, ArrayList::new);
    @NotNull String fieldName = refFieldInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
                           fieldName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 5;
    @NotNull RefFieldInfo refFieldInfo =
        new RefFieldInfo<>("parent", width, ArrayList::new);
    int fieldWidth = refFieldInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
                           fieldWidth);
  }

  @Test
  public void constructor_ShouldSetListSupplier() {
    @NotNull ArrayList<String> expectedList = new ArrayList<>();
    expectedList.add("first");
    expectedList.add("second");
    @NotNull RefFieldInfo<String> refFieldInfo =
        new RefFieldInfo<>("label", 20, () -> expectedList);
    @NotNull List<String> refFieldList = refFieldInfo.getListSupplier().get();
    Assert.assertEquals("constructor should set correct value of 'listSupplier'",
                           expectedList, refFieldList);
  }
}
