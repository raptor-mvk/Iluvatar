/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.layout;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Consumer;

public class JFXTabLayout extends JFXLayout {
	@NotNull
	private final TabPane root = new TabPane();


	@SuppressWarnings("WeakerAccess")
	public JFXTabLayout() {
		root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	}

	@Override
	public int registerViewService(@NotNull String serviceKey,
	                               @NotNull Runnable defaultViewSetter) {
		@NotNull ObservableList<Tab> tabList = getTabList();
		@NotNull Tab tab = new Tab(serviceKey);
		tab.setOnSelectionChanged((event) -> {
			if (tab.isSelected()) {
				Platform.runLater(defaultViewSetter::run);
			}
		});
		tabList.add(tab);
		return tabList.size() - 1;
	}

	@NotNull
	@Override
	public Consumer<Object> getViewUpdater(int serviceId) {
		return (content) -> {
			if (content instanceof Node) {
				@NotNull ObservableList<Tab> tabList = getTabList();
				@Nullable Tab tab = tabList.get(serviceId);
				if (tab != null) {
					@NotNull ScrollPane scrollPane = new ScrollPane();
					scrollPane.setContent(((Node) content));
					tab.setContent(scrollPane);
				}
			}
		};
	}

	@NotNull
	@Override
	public Consumer<Object> getListViewUpdater(int serviceId) {
		return (content) -> {
			if (content instanceof Node) {
				@NotNull ObservableList<Tab> tabList = getTabList();
				@Nullable Tab tab = tabList.get(serviceId);
				if (tab != null) {
					@NotNull ScrollPane scrollPane = new ScrollPane();
					scrollPane.setContent(((Node) content));
					scrollPane.setFitToWidth(true);
					tab.setContent(scrollPane);
				}
			}
		};
	}

	@Override
	public void show(int width, int height) {
		@NotNull Stage stage = getStage();
		stage.setScene(new Scene(root, width, height));
		stage.setResizable(false);
		stage.show();
	}

	@NotNull
	ObservableList<Tab> getTabList() {
		@Nullable ObservableList<Tab> tabList = root.getTabs();
		if (tabList == null) {
			throw new IluvatarRuntimeException("JFXTabLayout: tab list is null");
		}
		return tabList;
	}
}
