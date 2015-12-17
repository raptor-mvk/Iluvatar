/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class DateFieldInfo extends NamedFieldInfoImpl {
	public DateFieldInfo(@NotNull String name) {
		super(name);
	}

	@NotNull
	@Override
	public String getJFXFieldClassName() {
		return "ru.mvk.iluvatar.javafx.field.DateField";
	}
}
