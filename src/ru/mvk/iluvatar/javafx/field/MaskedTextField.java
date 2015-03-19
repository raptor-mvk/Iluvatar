/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Masked text field. Mask supports following special characters:
  # - any digit.
 */
abstract class MaskedTextField extends BasicSizedTextField {
  @NotNull
  private final List<Integer> digitPositions;
  @NotNull
  private StringBuilder value;
  @NotNull
  private final String defaultValue;
  private int position;
  @NotNull
  private Matcher fieldMatcher;
  @NotNull
  private Consumer<String> fieldMaskedUpdater = (value) -> {
  };

  MaskedTextField(@NotNull String mask) {
    super(mask.length());
    defaultValue = mask.replace('#', '0');
    value = new StringBuilder(defaultValue);
    setText(defaultValue);
    digitPositions = prepareDigitPositions(mask);
    fieldMatcher = prepareDefaultMatcher();
    setListeners();
  }

  @NotNull
  String getValue() {
    return value.toString();
  }

  void setValue(@NotNull String value) {
    int length = value.length();
    boolean result = length == digitPositions.size();
    for (int i = 0; i < length && result; i++) {
      int position = digitPositions.get(i);
      char digit = value.charAt(i);
      if (digit < '0' || digit > '9') {
        result = false;
      } else {
        this.value.setCharAt(position, digit);
      }
    }
    if (result) {
      @NotNull String text = this.value.toString();
      setText(text);
    }
  }

  void setFieldMatcher(@NotNull Matcher fieldMatcher) {
    this.fieldMatcher = fieldMatcher;
  }

  void setFieldMaskedUpdater(@NotNull Consumer<String> fieldMaskedUpdater) {
    this.fieldMaskedUpdater = fieldMaskedUpdater;
  }

  private List<Integer> prepareDigitPositions(@NotNull String mask) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0, length = mask.length(); i < length; i++) {
      if (mask.charAt(i) == '#') {
        result.add(i);
      }
    }
    return result;
  }

  private void setListeners() {
    setMouseListener();
    setSelectionListener();
    setKeyPressedListener();
    setKeyTypedListener();
    setKeyReleasedListener();
  }

  private void setSelectionListener() {
    @Nullable ReadOnlyObjectProperty selectionProperty = selectionProperty();
    if (selectionProperty == null) {
      throw new IluvatarRuntimeException("MaskedTextField: selectionProperty is null");
    }
    selectionProperty().addListener((observableValue, oldPropertyValue,
                                     newPropertyValue) -> {
      if (newPropertyValue.getLength() > 1) {
        position = 0;
        selectRange(0, 1);
      }
    });
  }

  private void setMouseListener() {
    setOnMouseClicked((event) -> {
      int startPosition = getCaretPosition();
      selectNearestPosition(startPosition);
    });
  }

  private void setKeyPressedListener() {
    setOnKeyPressed((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode != KeyCode.TAB) {
        event.consume();
      }
      if (keyCode == KeyCode.DELETE) {
        value = new StringBuilder(defaultValue);
        setText(defaultValue);
      } else if (keyCode == KeyCode.BACK_SPACE) {
        replacePreviousDigit('0');
      }
    });
  }

  private void setKeyTypedListener() {
    setOnKeyTyped((event) -> {
      event.consume();
      char key = getCharFromCharacter(event);
      if (key >= '0' && key <= '9') {
        replaceCurrentDigit(key);
      }
    });
  }

  private void setKeyReleasedListener() {
    setOnKeyReleased((event) -> {
      char key = getCharFromText(event);
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode != KeyCode.TAB) {
        event.consume();
      }
      processCaretMove(keyCode, key);
    });
  }

  private boolean isCorrect(@NotNull String value) {
    fieldMatcher.reset(value);
    return (value.length() <= getMaxLength()) && fieldMatcher.matches();
  }

  private char getCharFromCharacter(@NotNull KeyEvent event) {
    @NotNull String keyText = event.getCharacter();
    return getChar(keyText);
  }

  private char getCharFromText(@NotNull KeyEvent event) {
    @NotNull String keyText = event.getText();
    return getChar(keyText);
  }

  // returns ' ' for non-text char
  private char getChar(@NotNull String keyText) {
    @NotNull char key = ' ';
    if (keyText.length() > 0) {
      key = keyText.charAt(0);
    }
    return key;
  }

  private void processCaretMove(@NotNull KeyCode keyCode, char key) {
    if (keyCode == KeyCode.RIGHT || (key >= '0' && key <= '9')) {
      moveCaretRight();
    } else if (keyCode == KeyCode.LEFT || keyCode == KeyCode.BACK_SPACE) {
      moveCaretLeft();
    } else if (keyCode == KeyCode.DELETE || keyCode == KeyCode.HOME) {
      moveCaretHome();
    } else if (keyCode == KeyCode.END) {
      moveCaretEnd();
    }
  }

  private void moveCaretHome() {
    position = 0;
    int startPosition = digitPositions.get(position);
    positionCaret(startPosition);
    selectRange(startPosition, startPosition + 1);
  }

  private void moveCaretEnd() {
    position = digitPositions.size() - 1;
    int startPosition = digitPositions.get(position);
    positionCaret(startPosition);
    selectRange(startPosition, startPosition + 1);
  }

  private void moveCaretRight() {
    int digitPositionsSize = digitPositions.size();
    int nextPosition = digitPositions.get(digitPositionsSize - 1);
    // if position is the rightmost, then no moving
    if (position < digitPositionsSize - 1) {
      nextPosition = digitPositions.get(++position);
    }
    positionCaret(nextPosition);
    selectRange(nextPosition, nextPosition + 1);
  }

  private void moveCaretLeft() {
    int nextPosition = 0;
    if (position > 0) {
      nextPosition = digitPositions.get(--position);
    }
    positionCaret(nextPosition);
    selectRange(nextPosition, nextPosition + 1);
  }

  private void replacePreviousDigit(char digit) {
    if (position > 0) {
      int previousPosition = digitPositions.get(position - 1);
      replaceDigit(digit, previousPosition);
    }

  }

  private void replaceCurrentDigit(char digit) {
    int currentPosition = digitPositions.get(position);
    replaceDigit(digit, currentPosition);
  }

  private void replaceDigit(char digit, int position) {
    char currentDigit = value.charAt(position);
    value.setCharAt(position, digit);
    @NotNull String newValue = value.toString();
    if (isCorrect(newValue)) {
      fieldMaskedUpdater.accept(newValue);
    } else {
      value.setCharAt(position, currentDigit);
    }
    @NotNull String text = value.toString();
    int caretPosition = getCaretPosition();
    setText(text);
    positionCaret(caretPosition);
  }

  private void selectNearestPosition(int startPosition) {
    int digitPositionsCount = digitPositions.size();
    if (startPosition < digitPositions.get(0)) {
      position = 0;
    } else if (startPosition > digitPositions.get(digitPositionsCount - 1)) {
      position = digitPositionsCount - 1;
    } else {
      position = 0;
      while (startPosition > digitPositions.get(position)) {
        position++;
      }
    }
    int caretPosition = digitPositions.get(position);
    selectRange(caretPosition, caretPosition + 1);
  }

  @NotNull
  private Matcher prepareDefaultMatcher() {
    @NotNull Pattern pattern = Pattern.compile("^.*$");
    return pattern.matcher("");
  }
}
