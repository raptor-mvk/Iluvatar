/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.field.CheckBoxInfo;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.utils.CommonTestUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ViewInfoImplUnitTests {
  @Test
  public void constructor_ShouldSetFieldsCountToZero() {
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(Object.class);
    int fieldsCount = viewInfo.getFieldsCount();
    Assert.assertEquals("Constructor should set value of 'fieldsCount' to 0", 0,
        fieldsCount);
  }

  @Test
  public void constructor_ShouldSetEntityType() {
    @NotNull Class<Object> entityType = Object.class;
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(entityType);
    @NotNull Class<?> viewInfoEntityType = viewInfo.getEntityType();
    Assert.assertEquals("Constructor should set correct value of 'entityType'",
        entityType, viewInfoEntityType);
  }

  @Test(expected = IluvatarRuntimeException.class)
  public void getField_IllegalKey_ShouldThrowIluvatarRuntimeException() {
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(Object.class);
    viewInfo.getFieldInfo("name");
  }

  @Test
  public void addFieldInfo_ShouldIncreaseFieldsCount() {
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(Object.class);
    viewInfo.addFieldInfo("name", new CheckBoxInfo("Name"));
    int fieldsCount = viewInfo.getFieldsCount();
    Assert.assertEquals("addFieldInfo() should increase value of 'fieldsCount'", 1,
        fieldsCount);
  }

  @Test
  public void addFieldInfo_ShouldAddGettableFieldInfo() {
    @NotNull String key = "enabled";
    @NotNull NamedFieldInfo fieldInfo = new CheckBoxInfo("Enabled");
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(Object.class);
    viewInfo.addFieldInfo(key, fieldInfo);
    @NotNull NamedFieldInfo viewInfoFieldInfo = viewInfo.getFieldInfo(key);
    Assert.assertEquals("addFieldInfo(key, fieldInfo) should add fieldInfo " +
            "'fieldInfo', that is gettable by key 'key'", viewInfoFieldInfo,
        viewInfoFieldInfo);
  }

  @Test
  public void getIterator_ShouldMaintainOrderOfAddition() {
    @NotNull List<String> keyList = prepareKeyList();
    @NotNull ViewInfo<Object> viewInfo = prepareViewInfo(keyList);
    @NotNull Iterator<Entry<String, NamedFieldInfo>> fieldInfoIterator =
        viewInfo.getIterator();
    for (int i = 0; fieldInfoIterator.hasNext(); i++) {
      @Nullable String key = keyList.get(i);
      @Nullable Entry<String, NamedFieldInfo> entry = fieldInfoIterator.next();
      @NotNull String entryKey = CommonTestUtils.getEntryKey(entry);
      Assert.assertEquals("getIterator() should maintain order of addition", key,
          entryKey);
    }
  }

  @NotNull
  private ViewInfo<Object> prepareViewInfo(@NotNull List<String> keyList) {
    @NotNull ViewInfo<Object> viewInfo = new ViewInfoImpl<>(Object.class);
    for (String key : keyList) {
      viewInfo.addFieldInfo(key, new CheckBoxInfo(key));
    }
    return viewInfo;
  }

  @NotNull
  private List<String> prepareKeyList() {
    @NotNull ArrayList<String> keyList = new ArrayList<>();
    keyList.add("name");
    keyList.add("width");
    keyList.add("enabled");
    keyList.add("weight");
    return keyList;
  }
}
