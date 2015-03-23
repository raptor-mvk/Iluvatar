/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;

public class LowerStringColumnInfo extends StringColumnInfo {
  public LowerStringColumnInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value instanceof String) {
        int length = ((String) value).length();
        result = ((String) value).substring(length / 2);
      }
      return result;
    };
  }

  @NotNull
  @Override
  public Pos getJFXAlignment() {
    return Pos.CENTER_LEFT;
  }
}
