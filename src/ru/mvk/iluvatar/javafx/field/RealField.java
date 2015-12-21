/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.apache.commons.lang3.StringUtils;
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
	private final int fractionWidth;
	private final long multiplier;

	public RealField(@NotNull RealFieldInfo<Type> fieldInfo) {
		super(fieldInfo);
		int integerWidth = fieldInfo.getIntegerWidth();
		fractionWidth = fieldInfo.getFractionWidth();
		multiplier = (int) Math.pow(10.0, fieldInfo.getFractionWidth());
		formatString = "%." + fractionWidth + 'f';
		@NotNull String matcherString =
				String.format("^-?\\d{0,%d}(\\.\\d{0,%d})?$", integerWidth, fractionWidth);
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
		return String.format(Locale.ENGLISH, formatString,
				(double) value.longValue() / multiplier);
	}

	@Override
	protected Type convertValue(@NotNull String value) {
		int pointPos = value.indexOf('.');
		int newLength;
		if (pointPos > 0) {
			value = value.substring(0, pointPos) + value.substring(pointPos + 1);
			newLength = pointPos + fractionWidth;
		} else if (pointPos == 0) {
			value = value.substring(pointPos + 1);
			newLength = fractionWidth;
		} else {
			newLength = value.length() + fractionWidth;
		}
		return super.convertValue(StringUtils.rightPad(value, newLength, '0'));
	}
}
