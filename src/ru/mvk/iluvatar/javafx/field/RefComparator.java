/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RefComparator<Type> implements Comparator<Type> {
  @Override
  public int compare(Type item1, Type item2) {
    int result;
    if (item1 == null) {
      result = -1;
    } else if (item2 == null) {
      result = 1;
    } else {
      @NotNull String value1 = item1.toString().toLowerCase().replace('¸','å');
      @NotNull String value2 = item2.toString().toLowerCase().replace('¸','å');
      result = value1.compareTo(value2);
    }
    return result;
  }
}
