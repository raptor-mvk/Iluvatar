/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.view;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface LocalizedStringSupplier extends StringSupplier {
  boolean registerLocale(@NotNull Locale newLocale);

  boolean setLocale(@NotNull Locale newLocale);
}
