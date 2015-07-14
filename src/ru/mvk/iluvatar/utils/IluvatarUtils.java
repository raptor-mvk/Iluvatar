/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

public class IluvatarUtils {
  @NotNull
  public static <Type> Stream<Type> mapToTypedStream(@NotNull Collection<?> list,
                                                     @NotNull Class<Type> type) {
    @Nullable Stream<?> stream = list.stream();
    if (stream == null) {
      throw new IluvatarRuntimeException("VideoGuideUtils: stream is null");
    }
    @Nullable Stream<?> filteredStream = ((Stream<?>) stream).filter(type::isInstance);
    if (filteredStream == null) {
      throw new IluvatarRuntimeException("VideoGuideUtils: filtered stream is null");
    }
    @Nullable Stream<Type> result = filteredStream.map(type::cast);
    if (result == null) {
      throw new IluvatarRuntimeException("VideoGuideUtils: mapped stream is null");
    }
    return result;
  }

  @NotNull
  public static String normalizeString(@NotNull String value) {
    return value.toLowerCase().replace('ё', 'е');
  }
}
