/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.descriptor.column;

import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.RealDescriptor;

public class RationalColumnInfo extends PlainColumnInfo {
	private int fractionWidth;
	private long multiplier;
	@NotNull
	private String stringFormat;

	public RationalColumnInfo(@NotNull String name,
	                          @NotNull RealDescriptor realDescriptor) {
		super(name, realDescriptor.getFractionWidth() + realDescriptor.getIntegerWidth() + 2);
		fractionWidth = realDescriptor.getFractionWidth();
		multiplier = (long) Math.pow(10.0, fractionWidth);
		stringFormat = "%." + fractionWidth + "f";
	}

	@NotNull
	@Override
	public ViewFormatter getViewFormatter() {
		return (value) -> {
			@NotNull String result = "";
			if (value instanceof Number) {
				result = String.format(stringFormat, ((Number) value).doubleValue() / multiplier);
			}
			return result;
		};
	}

	@NotNull
	@Override
	public Pos getJFXAlignment() {
		return Pos.CENTER_RIGHT;
	}
}
