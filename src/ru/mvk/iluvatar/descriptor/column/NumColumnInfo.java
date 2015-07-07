/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class NumColumnInfo implements ColumnInfo {
  @NotNull
  private final String name;
  private final int width;

  public NumColumnInfo(@NotNull String name, int width) {
    if (width <= 0) {
      throw new IluvatarRuntimeException("StringColumnInfo: non-positive width");
    }
    this.name = name;
    this.width = width;
  }

  @Override
  public final int getWidth() {
    return width;
  }

  @NotNull
  @Override
  public final String getName() {
    return name;
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value != null) {
        result = value.toString();
      }
      return result;
    };
  }

  @NotNull
  @Override
  public Pos getJFXAlignment() {
    return Pos.CENTER_RIGHT;
  }
}
