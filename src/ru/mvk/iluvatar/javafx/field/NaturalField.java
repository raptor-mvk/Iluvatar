/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.NumberFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;

public class NaturalField<Type> extends SizedTextField implements Field<Type> {
  @NotNull
  private final Class<Type> type;
  @NotNull
  private final Matcher numberMatcher;
  @NotNull
  private final Matcher zeroEqualMatcher;
  @NotNull
  private Consumer<Type> fieldUpdater;

  public NaturalField(@NotNull NumberFieldInfo<Type> fieldInfo) {
    super(fieldInfo.getWidth());
    fieldUpdater = (value) -> {
    };
    type = fieldInfo.getType();
    @Nullable Class<?> fieldType = getClass();
    if (fieldType == null) {
      throw new IluvatarRuntimeException("NaturalField: fieldInfo class is null");
    }
    numberMatcher = FieldUtils.getNumberMatcher(fieldType);
    zeroEqualMatcher = FieldUtils.getZeroEqualMatcher(fieldType);
    setListener();
  }

  private void setListener() {
    @Nullable StringProperty fieldTextProperty = textProperty();
    if (fieldTextProperty == null) {
      throw new IluvatarRuntimeException("NaturalField: textProperty is null");
    }
    @NotNull ChangeListener<String> fieldChangeListener = prepareChangeListener();
    fieldTextProperty.addListener(fieldChangeListener);
  }

  @NotNull
  private ChangeListener<String> prepareChangeListener() {
    @NotNull Function<String, ?> typeCaster = FieldUtils.getTypeCaster(type);
    return (observableValue, oldValue, newValue) -> {
      if (isCorrect(newValue)) {
        if (isZeroEqual(newValue)) {
          newValue = "0";
        }
        @NotNull Type fieldValue = convertFieldValue(newValue, typeCaster);
        fieldUpdater.accept(fieldValue);
      } else {
        restoreOldValue(oldValue, newValue);
      }
    };
  }

  private boolean isCorrect(@NotNull String value) {
    numberMatcher.reset(value);
    return (value.length() <= getMaxLength()) && numberMatcher.matches();
  }

  private boolean isZeroEqual(@NotNull String value) {
    zeroEqualMatcher.reset(value);
    return zeroEqualMatcher.matches();
  }

  @NotNull
  private Type convertFieldValue(@NotNull String value,
                                 @NotNull Function<String, ?> typeCaster) {
    if (value.isEmpty()) {
      value = "0";
    }
    @Nullable Object result = typeCaster.apply(value);
    @Nullable Type typedResult = type.cast(result);
    if (typedResult == null) {
      throw new IluvatarRuntimeException("NaturalField: could not cast value to " +
          "correct type");
    }
    return typedResult;
  }

  @Override
  public void setFieldUpdater(@NotNull Consumer<Type> fieldUpdater) {
    this.fieldUpdater = fieldUpdater;
  }

  @Override
  public void setFieldValue(@NotNull Object value) {
    if (type.isInstance(value)) {
      @NotNull String text = value.toString();
      setText(text);
    } else {
      throw new IluvatarRuntimeException("NaturalField: incorrect value type");
    }
  }

}