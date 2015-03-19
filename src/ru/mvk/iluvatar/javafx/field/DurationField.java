/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx.field;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.SizedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationField extends MaskedTextField implements Field<Integer> {
  private static final int SECONDS_IN_HOUR = 3600;
  private static final int SECONDS_IN_MINUTE = 60;

  @NotNull
  private static String prepareMask(int length) {
    @NotNull StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; i++) {
      result.append('#');
    }
    return result.append(":##:##").toString();
  }

  public DurationField(@NotNull SizedFieldInfo fieldInfo) {
    super(prepareMask(fieldInfo.getWidth()));
    @NotNull Matcher matcher = prepareMatcher(fieldInfo.getWidth());
    setFieldMatcher(matcher);
  }

  @Override
  public void setFieldUpdater(@NotNull Consumer<Integer> fieldUpdater) {
    setFieldMaskedUpdater((value) -> {
      int fieldValue = decode(value);
      fieldUpdater.accept(fieldValue);
    });
  }

  @Override
  public void setFieldValue(@NotNull Object value) {
    if (value instanceof Integer) {
      @NotNull String text = encode((Integer) value);
      setValue(text);
    } else {
      throw new IluvatarRuntimeException("DurationField: incorrect value type");
    }
  }

  @NotNull
  private String encode(int value) {
    @NotNull StringBuilder result = new StringBuilder();
    int maxLength = getMaxLength();
    @NotNull String hours = leftPad(value / SECONDS_IN_HOUR, maxLength - 6);
    result.append(hours);
    value %= SECONDS_IN_HOUR;
    @NotNull String minutes = leftPad(value / SECONDS_IN_MINUTE, 2);
    @NotNull String seconds = leftPad(value % SECONDS_IN_MINUTE, 2);
    return result.append(minutes).append(seconds).toString();
  }

  private int decode(@NotNull String text) {
    @NotNull String[] parts = StringUtils.split(text, ':');
    int result = 0;
    for (String part : parts) {
      result *= 60;
      result += Integer.parseInt(part);
    }
    return result;
  }

  @NotNull
  private String leftPad(int value, int size) {
    @NotNull String result = Integer.toString(value);
    return StringUtils.leftPad(result, size, '0');
  }

  @NotNull
  private Matcher prepareMatcher(int length) {
    @NotNull StringBuilder patternStringBuilder = new StringBuilder("^");
    for (int i = 0; i < length; i++) {
      patternStringBuilder.append("\\d");
    }
    patternStringBuilder.append(":[0-5]\\d:[0-5]\\d$");
    @NotNull String patternString = patternStringBuilder.toString();
    @NotNull Pattern pattern = Pattern.compile(patternString);
    return pattern.matcher("");
  }
}
