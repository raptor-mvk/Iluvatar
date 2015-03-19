/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.exception;

import org.jetbrains.annotations.NotNull;

public class IluvatarRuntimeException extends RuntimeException {
  public IluvatarRuntimeException(@NotNull String message) {
    super(message);
  }
}
