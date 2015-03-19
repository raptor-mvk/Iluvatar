/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.view.LocalizedStringSupplier;
import ru.mvk.iluvatar.view.StringSupplier;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BundleStringSupplier implements LocalizedStringSupplier {
	@NotNull
	private final HashMap<Locale, ResourceBundle> resourceMap;
	@NotNull
	private final String resourceFileName;
	@Nullable
	private Locale locale;

	public BundleStringSupplier(@NotNull String resourceFileName) {
		resourceMap = new HashMap<>();
		this.resourceFileName = resourceFileName;
	}

	@Override
	public boolean registerLocale(@NotNull Locale newLocale) {
		boolean result = true;
		try {
			@Nullable ResourceBundle resourceBundle =
					ResourceBundle.getBundle(resourceFileName, newLocale);
			if (resourceBundle == null) {
				result = false;
			} else {
				resourceMap.put(newLocale, resourceBundle);
				if (locale == null) {
					setLocale(newLocale);
				}
			}
		} catch (MissingResourceException e) {
			result = false;
		}
		return result;
	}


	@Override
	public boolean setLocale(@NotNull Locale newLocale) {
		boolean result = true;
		if (resourceMap.get(newLocale) == null) {
			result = false;
		} else {
			locale = newLocale;
		}
		return result;
	}

	@NotNull
	@Override
	public String apply(@NotNull String stringId) {
		@NotNull String result = stringId;
		if (locale != null) {
			@Nullable ResourceBundle resourceBundle = resourceMap.get(locale);
			if (resourceBundle != null) {
				try {
					result = resourceBundle.getString(stringId);
				} catch (MissingResourceException ignored) {
				}
			}
		}
		return result;
	}
}
