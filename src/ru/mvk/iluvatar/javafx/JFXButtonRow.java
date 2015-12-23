/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */
package ru.mvk.iluvatar.javafx;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.StringSupplier;

import java.util.HashMap;

class JFXButtonRow extends HBox {
	@NotNull
	private final HashMap<String, Button> buttons = new HashMap<>();
	@NotNull
	private final StringSupplier stringSupplier;
	@NotNull
	private final IdGenerator idGenerator;

	JFXButtonRow(@NotNull StringSupplier stringSupplier,
	             @NotNull IdGenerator idGenerator) {
		this.stringSupplier = stringSupplier;
		this.idGenerator = idGenerator;
		setGaps();
	}

	void addButton(@NotNull String buttonName) {
		@NotNull String buttonId = idGenerator.getButtonId(buttonName);
		@NotNull String buttonCaption = stringSupplier.apply(buttonName);
		@NotNull Button button = new Button(buttonCaption);
		button.setId(buttonId);
		buttons.put(buttonName, button);
		getChildren().add(button);
	}

	void setButtonHandler(@NotNull String buttonName, @NotNull Runnable handler) {
		getButton(buttonName).setOnAction(event -> handler.run());
	}

	void fireButton(@NotNull String buttonName) {
		getButton(buttonName).fire();
	}

	void requestButtonFocus(@NotNull String buttonName) {
		getButton(buttonName).requestFocus();
	}

	private void setGaps() {
		// 5.0 is empirically selected spacing and padding
		setSpacing(5.0);
		setPadding(new Insets(5.0));
	}

	@NotNull
	private Button getButton(@NotNull String buttonName) {
		@Nullable Button result = buttons.get(buttonName);
		if (result == null) {
			@NotNull String errorMessage =
					String.format("JFXButtonRow: button '%s' was not found", buttonName);
			throw new IluvatarRuntimeException(errorMessage);
		}
		return result;
	}
}
