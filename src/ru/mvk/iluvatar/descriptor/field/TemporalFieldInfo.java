/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

public interface TemporalFieldInfo<Type> extends SizedFieldInfo {
	@NotNull
	Type getDefaultValue();

	@NotNull
	DateTimeFormatter getFormatter();
}
