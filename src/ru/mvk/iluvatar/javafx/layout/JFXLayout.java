/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.layout;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.ViewInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.javafx.JFXIdGenerator;
import ru.mvk.iluvatar.javafx.JFXListView;
import ru.mvk.iluvatar.javafx.JFXView;
import ru.mvk.iluvatar.view.*;

/* TODO: default idGenerator? */
public abstract class JFXLayout implements Layout {
	@Nullable
	private Stage stage;
	@NotNull
	private StringSupplier stringSupplier = (value) -> value;
	@NotNull
	private IdGenerator idGenerator = new JFXIdGenerator(Object.class);

	@NotNull
	@Override
	public <EntityType> ListView<EntityType> getListView(@NotNull ListViewInfo<EntityType>
			                                                     listViewInfo) {
		return new JFXListView<>(listViewInfo, stringSupplier, idGenerator);
	}

	@NotNull
	@Override
	public <EntityType> View<EntityType> getView(@NotNull ViewInfo<EntityType> viewInfo) {
		return new JFXView<>(viewInfo, stringSupplier, idGenerator);
	}

	@SuppressWarnings("WeakerAccess")
	public void setStage(@Nullable Stage stage) {
		this.stage = stage;
	}

	@Override
	public void setStringSupplier(@NotNull StringSupplier stringSupplier) {
		this.stringSupplier = stringSupplier;
	}

	@Override
	public void setIdGenerator(@NotNull IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@NotNull
	final Stage getStage() {
		if (stage == null) {
			throw new IluvatarRuntimeException("Stage was not set");
		}
		return stage;
	}
}
