/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

abstract class BasicSizedTextField extends TextField {
  private final int maxLength;

  BasicSizedTextField(int maxLength) {
    if (maxLength <= 0) {
      throw new IluvatarRuntimeException("BasicSizedTextField: non-positive width");
    }
    this.maxLength = maxLength;
    // force double length for too short fields
    if (maxLength > 5) {
      setAllWidths(maxLength);
    } else {
      setAllWidths(maxLength * 2);
    }
  }

  private void setAllWidths(int length) {
    // 0.75 is a ratio of conversion from font size to average letter width
    Font defaultFont = Font.getDefault();
    double width = length * defaultFont.getSize() * 0.75;
    setMinWidth(width);
    setMaxWidth(width);
    setMaxWidth(width);
  }

  final int getMaxLength() {
    return maxLength;
  }
}
