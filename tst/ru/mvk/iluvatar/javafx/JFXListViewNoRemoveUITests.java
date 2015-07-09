/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.ListViewInfoImpl;
import ru.mvk.iluvatar.descriptor.column.*;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.UITests;
import ru.mvk.iluvatar.view.ListView;

import java.util.ArrayList;

public class JFXListViewNoRemoveUITests extends UITests<ListView<Student>> {
  @NotNull
  private final ListViewInfo<Student> listViewInfo = prepareListViewInfo();

  @Test
  public void listViewInfoRemoveAllowedIsFalse_RemoveButtonShouldBeInvisible() {
    @NotNull ListView<Student> listView = getObjectUnderTest();
    @NotNull String removeButtonId = listView.getRemoveButtonId();
    Assert.assertFalse("Remove button should be invisible, when " +
                           "listViewInfo.isRemoveAllowed is false",
                          stage.getScene().lookup('#' + removeButtonId).isVisible());
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull JFXListView<Student> listView = (JFXListView<Student>) prepareListView();
    setObjectUnderTest(listView);
    @Nullable GridPane result = listView.getListView();
    return (result == null) ? new GridPane() : result;
  }

  @NotNull
  private ListView<Student> prepareListView() {
    ListView<Student> result = new JFXListView<>(listViewInfo, (value) -> value);
    result.setListSupplier(ArrayList::new);
    return result;
  }

  @NotNull
  private ListViewInfo<Student> prepareListViewInfo() {
    @NotNull ListViewInfo<Student> result = new ListViewInfoImpl<>(Student.class);
    result.addColumnInfo("id", new NumColumnInfo("id", 10));
    result.addColumnInfo("name", new StringColumnInfo("name", 50));
    result.addColumnInfo("gpa", new NumColumnInfo("gpa", 5));
    result.addColumnInfo("penalty", new NumColumnInfo("penalty", 5));
    result.addColumnInfo("graduated", new BooleanColumnInfo("graduated", 3));
    result.addColumnInfo("fileSize", new FileSizeColumnInfo("fileSize", 10));
    result.addColumnInfo("lecturesTime", new DurationColumnInfo("lecturesTime", 8));
    result.setRemoveAllowed(false);
    return result;
  }
}
