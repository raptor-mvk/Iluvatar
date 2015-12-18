/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.RealFieldInfo;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RealField<Type extends Number> extends NaturalField<Type> {
	@NotNull
	private final String formatString;
	@NotNull
	private final Matcher realMatcher;
	@NotNull
	private final Matcher zeroMatcher;

	public RealField(@NotNull RealFieldInfo<Type> fieldInfo) {
		super(fieldInfo);
		@NotNull String fractionWidth = Integer.toString(fieldInfo.getFractionWidth());
		@NotNull String maxWidth = Integer.toString(getMaxLength());
		formatString = "%." + fractionWidth + 'f';
		@NotNull String matcherString = "^(?=^-?\\d*\\.?\\d{0," + fractionWidth +
				"}$)[-\\.\\d]{0," + maxWidth + "}$";
		realMatcher = Pattern.compile(matcherString).matcher("");
		zeroMatcher = Pattern.compile("^-?\\.?$").matcher("");
	}

	@Override
	boolean check(@NotNull String value) {
		return realMatcher.reset(value).matches();
	}

	@Override
	boolean isZeroValued(@NotNull String value) {
		return zeroMatcher.reset(value).matches();
	}

	@Override
	String valueToString(@NotNull Type value) {
		return String.format(Locale.ENGLISH, formatString, value);
	}
}
