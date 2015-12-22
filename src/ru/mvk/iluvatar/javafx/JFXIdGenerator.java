/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.view.IdGenerator;

/* TODO: tests */
public class JFXIdGenerator implements IdGenerator {
	@NotNull
	private final String entityName;

	public JFXIdGenerator(@NotNull Class<?> entityType) {
		entityName = entityType.getSimpleName();
	}

	@NotNull
	@Override
	public String getTableId() {
		return String.format("%s-list", entityName);
	}

	@NotNull
	@Override
	public String getButtonId(@NotNull String buttonName) {
		return String.format("%s-%s-button", entityName, buttonName);
	}

	@NotNull
	@Override
	public String getFieldId(@NotNull String key) {
		return String.format("%s-%s-field", entityName, key);
	}

	@NotNull
	@Override
	public String getLabelId(@NotNull String key) {
		return String.format("%s-%s-label", entityName, key);
	}
}
