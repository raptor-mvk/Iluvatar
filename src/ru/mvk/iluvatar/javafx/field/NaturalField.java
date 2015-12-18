/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.NumberFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalField<Type extends Number> extends SizedTextField<Type> {
	@NotNull
	private final Matcher naturalMatcher;

	public NaturalField(@NotNull NumberFieldInfo<Type> fieldInfo) {
		super(fieldInfo.getWidth(), fieldInfo.getType());
		naturalMatcher = Pattern.compile("^(\\d){0," + getMaxLength() + "}$").matcher("");
	}

	@Override
	boolean check(@NotNull String value) {
		return naturalMatcher.reset(value).matches();
	}

	boolean isZeroValued(@NotNull String value) {
		return value.isEmpty();
	}

	@Override
	protected Type convertValue(@NotNull String value) {
		@NotNull Type result;
		if (isZeroValued(value)) {
			value = "0";
		}
		try {
			@NotNull Constructor<Type> constructor =
					getType().getDeclaredConstructor(String.class);
			result = constructor.newInstance(value);
		} catch (NoSuchMethodException | InvocationTargetException |
				InstantiationException | IllegalAccessException e) {
			throw new IluvatarRuntimeException("NaturalField: could not convert value");
		}
		return result;
	}
}