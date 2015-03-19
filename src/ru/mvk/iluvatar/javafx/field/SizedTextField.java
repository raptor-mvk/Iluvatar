/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;

abstract class SizedTextField extends BasicSizedTextField {
  private int caretPositionShift;

  SizedTextField(int maxLength) {
    super(maxLength);
    caretPositionShift = 0;
    setKeyListener();
  }

  final void restoreOldValue(@NotNull String oldValue,
                             @NotNull String newValue) {
    int newLength = newValue.length();
    int oldLength = oldValue.length();
    int caretPosition = getCaretPosition();
    caretPositionShift = (newLength > oldLength && caretPosition < oldLength) ?
        oldLength - newLength : 0;
    setText(oldValue);
  }

  private void setKeyListener() {
    setOnKeyReleased((event) -> {
      int caretPosition = getCaretPosition();
      if (caretPositionShift != 0) {
        positionCaret(caretPosition + caretPositionShift);
        caretPositionShift = 0;
      }
    });
  }
}
