/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.descriptor.column.NumColumnInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.utils.CommonTestUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ListViewInfoImplUnitTests {
  @Test
  public void constructor_ShouldSetColumnsCountToZero() {
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(Object.class);
    int columnsCount = listViewInfo.getColumnsCount();
    Assert.assertEquals("Constructor should set value of 'columnsCount' to 0", 0,
                           columnsCount);
  }

  @Test
  public void constructor_ShouldSetEntityType() {
    @NotNull Class<Object> entityType = Object.class;
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(entityType);
    @NotNull Class<?> listViewInfoEntityType = listViewInfo.getEntityType();
    Assert.assertEquals("Constructor should set correct value of 'entityType'",
                           entityType, listViewInfoEntityType);
  }


  @Test(expected = IluvatarRuntimeException.class)
  public void getColumn_IllegalKey_ShouldThrowIluvatarRuntimeException() {
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(Object.class);
    listViewInfo.getColumnInfo("width");
  }

  @Test
  public void addColumnInfo_ShouldIncreaseColumnsCount() {
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(Object.class);
    listViewInfo.addColumnInfo("state", new NumColumnInfo("State", 10));
    int columnsCount = listViewInfo.getColumnsCount();
    Assert.assertEquals("addFieldInfo() should increase value of 'fieldsCount'", 1,
                           columnsCount);
  }

  @Test
  public void addColumnInfo_ShouldAddGettableColumnInfo() {
    @NotNull String key = "price";
    @NotNull ColumnInfo columnInfo = new NumColumnInfo("Price", 7);
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(Object.class);
    listViewInfo.addColumnInfo(key, columnInfo);
    @NotNull ColumnInfo listViewInfoColumnInfo = listViewInfo.getColumnInfo(key);
    Assert.assertEquals("addColumnInfo(key, columnInfo) should add columnInfo " +
                            "'columnInfo', that is gettable by key 'key'", columnInfo,
                           listViewInfoColumnInfo);
  }

  @Test
  public void getIterator_ShouldMaintainOrderOfAddition() {
    @NotNull List<String> keyList = prepareKeyList();
    @NotNull ListViewInfo<Object> listViewInfo = prepareListViewInfo(keyList);
    @NotNull Iterator<Entry<String, ColumnInfo>> columnInfoIterator =
        listViewInfo.getIterator();
    for (int i = 0; columnInfoIterator.hasNext(); i++) {
      @Nullable String key = keyList.get(i);
      @Nullable Entry<String, ColumnInfo> entry = columnInfoIterator.next();
      @NotNull String entryKey = CommonTestUtils.getEntryKey(entry);
      Assert.assertEquals("getIterator() should maintain order of addition", key,
                             entryKey);
    }
  }

  @NotNull
  private ListViewInfo<Object> prepareListViewInfo(@NotNull List<String> keyList) {
    @NotNull ListViewInfo<Object> listViewInfo = new ListViewInfoImpl<>(Object.class);
    for (String key : keyList) {
      listViewInfo.addColumnInfo(key, new NumColumnInfo(key, 10));
    }
    return listViewInfo;
  }

  @NotNull
  private List<String> prepareKeyList() {
    @NotNull ArrayList<String> keyList = new ArrayList<>();
    keyList.add("id");
    keyList.add("name");
    keyList.add("quantity");
    keyList.add("price");
    keyList.add("sum");
    return keyList;
  }
}
