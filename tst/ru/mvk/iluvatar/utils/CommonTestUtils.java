/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map.Entry;

public class CommonTestUtils {
  @NotNull
  public static <Type> String getEntryKey(@Nullable Entry<String, Type> entry) {
    if (entry == null) {
      throw new RuntimeException("Iterator entry is null");
    }
    @Nullable String result = entry.getKey();
    if (result == null) {
      throw new RuntimeException("Entry key is null");
    }
    return result;
  }

  @NotNull
  public static <Type> Type getEntryValue(@Nullable Entry<String, Type> entry) {
    if (entry == null) {
      throw new RuntimeException("Iterator entry is null");
    }
    @Nullable Type result = entry.getValue();
    if (result == null) {
      throw new RuntimeException("Entry value is null");
    }
    return result;
  }

}
