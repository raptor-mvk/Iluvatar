/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.NumberFieldInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntegerField<Type> extends NaturalField<Type> {
	@NotNull
	private final Matcher integerMatcher;

	public IntegerField(@NotNull NumberFieldInfo<Type> fieldInfo) {
		super(fieldInfo);
		@NotNull String maxWidth = Integer.toString(getMaxLength());
		@NotNull String matcherString = "^(?=^-?\\d*$)[-\\.\\d]{0," + maxWidth + "}$";
		integerMatcher = Pattern.compile(matcherString).matcher("");
	}

	@Override
	protected boolean isZeroValued(@NotNull String value) {
		return value.isEmpty() || value.equals("-");
	}

	@Override
	protected boolean check(@NotNull String value) {
		return integerMatcher.reset(value).matches();
	}
}