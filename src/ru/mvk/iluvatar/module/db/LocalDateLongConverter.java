/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.module.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;

/* TODO: tests */
@Converter
public class LocalDateLongConverter implements AttributeConverter<LocalDate, Long> {
	@Override
	@NotNull
	public Long convertToDatabaseColumn(@Nullable LocalDate date) {
		return date == null ? 0 : date.toEpochDay();
	}

	@Override
	@NotNull
	public LocalDate convertToEntityAttribute(@Nullable Long value) {
		long longValue = value == null ? 0 : value;
		return LocalDate.ofEpochDay(longValue);
	}
}
