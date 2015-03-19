/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;

public class PowerMockUtils {
  @NotNull
  public static <Type> Type mock(@NotNull Class<Type> type) {
    @Nullable Type result = PowerMockito.mock(type);
    if (result == null) {
      throw new RuntimeException("Mock result is null");
    }
    return result;
  }

  @NotNull
  public static <Type> Type mock(@NotNull Class<Type> classToMock,
                                 @NotNull Answer<?> defaultAnswer) {
    @Nullable Type result = PowerMockito.mock(classToMock, defaultAnswer);
    if (result == null) {
      throw new RuntimeException("Mock result is null");
    }
    return result;
  }

  @NotNull
  public static <Type> OngoingStubbing<Type> when(@Nullable Type methodCall) {
    @Nullable OngoingStubbing<Type> result = PowerMockito.when(methodCall);
    if (result == null) {
      throw new RuntimeException("When result is null");
    }
    return result;
  }

  @NotNull
  public static <Type> Type verify(@Nullable Type mock) {
    @Nullable Type result = Mockito.verify(mock);
    if (result == null) {
      throw new RuntimeException("Verify result is null");
    }
    return result;
  }

  @NotNull
  public static <Type> Type doNothingWhen(@Nullable Type mock) {
    @Nullable PowerMockitoStubber powerMockitoStubber = PowerMockito.doNothing();
    if (powerMockitoStubber == null) {
      throw new RuntimeException("DoNothing returned null");
    }
    return executeWhen(powerMockitoStubber, mock);
  }

  @NotNull
  public static <Type> Type doReturnWhen(@Nullable Object toBeReturned,
                                         @Nullable Type mock) {
    @Nullable PowerMockitoStubber powerMockitoStubber =
        PowerMockito.doReturn(toBeReturned);
    if (powerMockitoStubber == null) {
      throw new RuntimeException("DoReturns returned null");
    }
    return executeWhen(powerMockitoStubber, mock);
  }

  @NotNull
  private static <Type> Type executeWhen(@NotNull
                                         PowerMockitoStubber powerMockitoStubber,
                                         @Nullable Type mock) {
    @Nullable Type result = powerMockitoStubber.when(mock);
    if (result == null) {
      throw new RuntimeException("When result is null");
    }
    return result;
  }

  @NotNull
  public static <Type> Type spy(Type object) {
    @Nullable Type result = PowerMockito.spy(object);
    if (result == null) {
      throw new RuntimeException("Spy returned null");
    }
    return result;
  }

  @NotNull
  public static <Type> OngoingStubbing<Type> whenNew(@NotNull Class<Type> type,
                                                     @Nullable Object... arguments) {
    try {
      @Nullable OngoingStubbing<Type> result =
          PowerMockito.whenNew(type).withArguments(arguments);
      if (result == null) {
        throw new RuntimeException("WhenNew result is null");
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
