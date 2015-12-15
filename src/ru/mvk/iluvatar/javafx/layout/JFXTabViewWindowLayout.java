/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.layout;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class JFXTabViewWindowLayout extends JFXTabLayout {
  @NotNull
  private final ScrollPane viewRoot = new ScrollPane();
  @NotNull
  private final Stage viewWindowStage = new Stage(StageStyle.UTILITY);

  public JFXTabViewWindowLayout(int viewWindowWidth, int viewWindowHeight) {
    @NotNull Scene viewScene = new Scene(viewRoot, viewWindowWidth, viewWindowHeight);
    viewWindowStage.setScene(viewScene);
    viewWindowStage.setAlwaysOnTop(true);
    viewWindowStage.setResizable(false);
    viewWindowStage.initModality(Modality.WINDOW_MODAL);
  }

  @NotNull
  @Override
  public Consumer<Object> getViewUpdater(int serviceId) {
    return (content) -> {
      if (content instanceof Node) {
        viewRoot.setContent((Node) content);
        viewWindowStage.show();
      }
    };
  }

  @Override
  public void setStage(@Nullable Stage stage) {
    super.setStage(stage);
    viewWindowStage.initOwner(stage);
  }
}
