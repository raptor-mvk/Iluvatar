/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.utils.IOTestUtils;
import ru.mvk.iluvatar.utils.PowerMockUtils;
import ru.mvk.iluvatar.view.LocalizedStringSupplier;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class BundleStringSupplierIntegrationTests {
	@NotNull
	private static final String PATH = "out\\test\\Iluvatar";
	@NotNull
	private static final String FILENAME = "resources";
	@NotNull
	private static final Locale LOCALE = Locale.ENGLISH;
	@NotNull
	private static final String RESOURCE_FILENAME = "resources_en.properties";
	@NotNull
	private static final String CODE_PAGE = "utf-8";
	@NotNull
	private static final String RESOURCE_KEY = "Consumer";
	@NotNull
	private static final String RESOURCE_VALUE = "Customer";

	@Test
	public void registerLocale_WrongPropertiesFileName_ShouldReturnFalse() {
		@NotNull LocalizedStringSupplier stringSupplier =
				new BundleStringSupplier("bad_file");
		boolean result = stringSupplier.registerLocale(LOCALE);
		Assert.assertFalse("registerLocale() should return false, when corresponding " +
				"properties file is not found", result);
	}

	@Test
	public void registerLocale_CorrectPropertiesFileName_ShouldReturnTrue() {
		try {
			prepareResourceFile();
			@NotNull LocalizedStringSupplier stringSupplier =
					new BundleStringSupplier(FILENAME);
			boolean result = stringSupplier.registerLocale(LOCALE);
			Assert.assertTrue("registerLocale() should return true, when corresponding " +
					"properties file exists", result);
		} finally {
			deleteResourceFile();
		}
	}

	@Test
	public void registerLocale_LocaleWasNotSet_ShouldCallSetLocale() {
		try {
			prepareResourceFile();
			@NotNull LocalizedStringSupplier stringSupplier =
					PowerMockUtils.spy(new BundleStringSupplier(FILENAME));
			stringSupplier.registerLocale(LOCALE);
			PowerMockUtils.verify(stringSupplier).setLocale(LOCALE);
		} finally {
			deleteResourceFile();
		}
	}

	@Test
	public void setLocale_LocaleNotRegistered_ShouldReturnFalse() {
		@NotNull LocalizedStringSupplier stringSupplier =
				new BundleStringSupplier("bad_file");
		boolean result = stringSupplier.setLocale(LOCALE);
		Assert.assertFalse("setLocale() should return false, when locale has not been " +
				"registered yet", result);
	}

	@Test
	public void setLocale_RegisteredLocale_ShouldReturnTrue() {
		try {
			prepareResourceFile();
			@NotNull LocalizedStringSupplier stringSupplier = prepareLocalizedStringSupplier();
			boolean result = stringSupplier.setLocale(LOCALE);
			Assert.assertTrue("setLocale() should return true, when locale has been " +
					"successfully registered before", result);
		} finally {
			deleteResourceFile();
		}
	}

	@Test
	public void apply_ResourceNameAndLocaleNotSet_ShouldReturnResourceName() {
		@NotNull LocalizedStringSupplier stringSupplier = new BundleStringSupplier("strings");
		@NotNull String resourceName = "Resource";
		@NotNull String result = stringSupplier.apply(resourceName);
		Assert.assertEquals("apply(resourceName) should return resourceName when locale " +
				"was not set", resourceName, result);
	}

	@Test
	public void apply_ResourceNameNotFound_ShouldReturnResourceName() {
		try {
			prepareResourceFile();
			@NotNull LocalizedStringSupplier stringSupplier = prepareLocalizedStringSupplier();
			@NotNull String resourceName = new StringBuilder(RESOURCE_KEY).reverse().toString();
			@NotNull String result = stringSupplier.apply(resourceName);
			Assert.assertEquals("apply(resourceName) should return resourceName when locale " +
					"was not set", resourceName, result);
		} finally {
			deleteResourceFile();
		}
	}

	@Test
	public void apply_ResourceName_ShouldReturnResourceValue() {
		try {
			prepareResourceFile();
			@NotNull LocalizedStringSupplier stringSupplier = prepareLocalizedStringSupplier();
			@NotNull String result = stringSupplier.apply(RESOURCE_KEY);
			Assert.assertEquals("apply(resourceName) should return resourceName when locale " +
					"was not set", RESOURCE_VALUE, result);
		} finally {
			deleteResourceFile();
		}
	}

	private void prepareResourceFile() {
		@Nullable Writer writer = null;
		try {
			writer = IOTestUtils.getFileWriter(PATH, RESOURCE_FILENAME, CODE_PAGE);
			writer.write(RESOURCE_KEY + "=" + RESOURCE_VALUE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ignored) {
			}
		}
	}

	@NotNull
	private LocalizedStringSupplier prepareLocalizedStringSupplier() {
		@NotNull LocalizedStringSupplier stringSupplier = new BundleStringSupplier(FILENAME);
		stringSupplier.registerLocale(LOCALE);
		return stringSupplier;
	}

	private void deleteResourceFile() {
		IOTestUtils.deleteFile(PATH, RESOURCE_FILENAME);
	}
}
