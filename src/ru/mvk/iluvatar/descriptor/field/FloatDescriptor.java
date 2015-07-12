/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

public class FloatDescriptor {
  private final int maxWidth;

  private final int fractionWidth;

  public FloatDescriptor(int maxWidth, int fractionWidth) {
    if (maxWidth <= 0 || fractionWidth <= 0 || maxWidth - 1 < fractionWidth) {
      throw new IluvatarRuntimeException("FloatDescriptor: wrong widths");
    }
    this.maxWidth = maxWidth;
    this.fractionWidth = fractionWidth;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public int getFractionWidth() {
    return fractionWidth;
  }
}