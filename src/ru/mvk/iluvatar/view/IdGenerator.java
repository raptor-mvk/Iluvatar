/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.view;

import org.jetbrains.annotations.NotNull;

public interface IdGenerator {
	@NotNull
	String getTableId();

	@NotNull
	String getButtonId(@NotNull String buttonName);

	@NotNull
	String getFieldId(@NotNull String key);

	@NotNull
	String getLabelId(@NotNull String key);

}
