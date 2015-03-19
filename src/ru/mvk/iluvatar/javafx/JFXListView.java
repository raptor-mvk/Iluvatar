/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.view.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JFXListView<EntityType> implements ListView<EntityType> {
  @NotNull
  private final GridPane gridPane;
  @NotNull
  private final TableView<EntityType> tableView;
  @NotNull
  private final Button addButton;
  @NotNull
  private final Button editButton;
  @NotNull
  private final Button removeButton;
  @NotNull
  private final ListViewInfo<EntityType> listViewInfo;
  @NotNull
  private final String entityClassName;
  @NotNull
  private Consumer<EntityType> selectedEntitySetter = (entity) -> {
  };
  @NotNull
  private Consumer<Integer> selectedIndexSetter = (index) -> {
  };
  @NotNull
  private Runnable addButtonHandler = () -> {
  };
  @NotNull
  private Runnable editButtonHandler = () -> {
  };
  @NotNull
  private Runnable removeButtonHandler = () -> {
  };
  @NotNull
  private Supplier<List<EntityType>> listSupplier = ArrayList::new;

  public JFXListView(@NotNull ListViewInfo<EntityType> listViewInfo) {
    @NotNull Class<EntityType> entityType = listViewInfo.getEntityType();
    entityClassName = entityType.getSimpleName();
    this.listViewInfo = listViewInfo;
    addButton = prepareAddButton();
    editButton = prepareEditButton();
    removeButton = prepareRemoveButton();
    tableView = new TableView<>();
    gridPane = prepareGridPane();
    prepareTableView();
    setKeyPressedListener();
    setKeyReleasedListener();
  }

  @Override
  @NotNull
  public final String getTableId() {
    return entityClassName + "-list";
  }

  @Override
  @NotNull
  public final String getAddButtonId() {
    return entityClassName + "-add-button";
  }

  @Override
  @NotNull
  public final String getEditButtonId() {
    return entityClassName + "-edit-button";
  }

  @Override
  @NotNull
  public final String getRemoveButtonId() {
    return entityClassName + "-remove-button";
  }

  @Nullable
  @Override
  public GridPane getListView() {
    @NotNull List<EntityType> objectList = listSupplier.get();
    @NotNull ObservableList<EntityType> objectObservableList =
        FXCollections.observableList(objectList);
    tableView.setItems(objectObservableList);
    clearSelection();
    return gridPane;
  }

  @Override
  public void refreshTable() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> columnList =
        tableView.getColumns();
    if (columnList == null) {
      throw new IluvatarRuntimeException("JFXListView: column list is null");
    }
    @Nullable TableColumn<EntityType, ?> column = columnList.get(0);
    if (column != null) {
      column.setVisible(false);
      column.setVisible(true);
    }
  }

  @Override
  public void setAddButtonHandler(@NotNull Runnable handler) {
    addButtonHandler = handler;
    addButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setEditButtonHandler(@NotNull Runnable handler) {
    editButtonHandler = handler;
    editButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setRemoveButtonHandler(@NotNull Runnable handler) {
    removeButtonHandler = handler;
    removeButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setSelectedEntitySetter(@NotNull Consumer<EntityType> entitySetter) {
    selectedEntitySetter = entitySetter;
  }

  @Override
  public void setSelectedIndexSetter(@NotNull Consumer<Integer> indexSetter) {
    selectedIndexSetter = indexSetter;
  }


  @Override
  public void setListSupplier(@NotNull Supplier<List<EntityType>> listSupplier) {
    this.listSupplier = listSupplier;
  }

  @Override
  public void selectRowByIndex(int rowIndex) {
    @NotNull TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    int itemsCount = getTableViewItemsCount();
    if ((rowIndex >= 0) && (rowIndex < itemsCount)) {
      tableViewSelectionModel.select(rowIndex);
      editButton.setDisable(false);
      removeButton.setDisable(false);
    } else {
      tableViewSelectionModel.clearSelection();
      editButton.setDisable(true);
      removeButton.setDisable(true);
    }
  }

  @Override
  public void selectRowByEntity(@Nullable EntityType entity) {
    if (entity != null) {
      trySelect(entity);
      editButton.setDisable(false);
      removeButton.setDisable(false);
    } else {
      @NotNull TableViewSelectionModel<EntityType> tableViewSelectionModel =
          getTableViewSelectionModel();
      tableViewSelectionModel.clearSelection();
      editButton.setDisable(true);
      removeButton.setDisable(true);
    }
  }

  @Override
  public void scrollToIndex(int index) {
    int itemsCount = getTableViewItemsCount();
    if ((index >= 0) && (index < itemsCount)) {
      tableView.scrollTo(index);
    }
  }

  @Override
  public void scrollToEntity(@Nullable EntityType entity) {
    if (entity != null) {
      tableView.scrollTo(entity);
    }
  }

  @Override
  public void clearSelection() {
    selectRowByIndex(-1);
  }

  private void trySelect(@NotNull EntityType entity) {
    @NotNull TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    tableViewSelectionModel.select(entity);
    int selectedIndex = tableViewSelectionModel.getSelectedIndex();
    @Nullable ObservableList<EntityType> itemList = tableView.getItems();
    if (itemList == null) {
      throw new IluvatarRuntimeException("JFXListView: itemList is null");
    }
    @Nullable EntityType selectedEntity = null;
    if (selectedIndex >= 0 && selectedIndex < itemList.size()) {
      selectedEntity = itemList.get(selectedIndex);
    }
    if (!entity.equals(selectedEntity)) {
      tableViewSelectionModel.clearSelection();
    }
  }

  private void setKeyPressedListener() {
    gridPane.setOnKeyPressed((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.DELETE ||
          keyCode == KeyCode.INSERT) {
        event.consume();
      }
    });
  }

  private void setKeyReleasedListener() {
    gridPane.setOnKeyReleased((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.DELETE ||
          keyCode == KeyCode.INSERT) {
        event.consume();
        if (keyCode == KeyCode.ENTER) {
          runEditButtonHandler();
        } else if (keyCode == KeyCode.DELETE) {
          runRemoveButtonHandler();
        } else if (keyCode == KeyCode.INSERT) {
          addButtonHandler.run();
        }
      }
    });
  }

  private void runEditButtonHandler() {
    if (!editButton.isDisabled()) {
      editButtonHandler.run();
    }
  }

  private void runRemoveButtonHandler() {
    if (!removeButton.isDisabled()) {
      removeButtonHandler.run();
    }
  }

  @NotNull
  private Button prepareAddButton() {
    @NotNull String addButtonId = getAddButtonId();
    @NotNull Button result = new Button("Добавить");
    result.setId(addButtonId);
    return result;
  }

  @NotNull
  private Button prepareEditButton() {
    @NotNull String editButtonId = getEditButtonId();
    @NotNull Button result = new Button("Изменить");
    result.setId(editButtonId);
    return result;
  }

  @NotNull
  private Button prepareRemoveButton() {
    @NotNull String removeButtonId = getRemoveButtonId();
    @NotNull Button result = new Button("Удалить");
    result.setId(removeButtonId);
    return result;
  }

  @NotNull
  private GridPane prepareGridPane() {
    @NotNull GridPane result = new GridPane();
    // 5.0 is empirically selected gap and padding
    result.setHgap(5.0);
    result.setVgap(5.0);
    result.setPadding(new Insets(5.0));
    result.add(tableView, 0, 0, 3, 1);
    result.add(addButton, 0, 1);
    result.add(editButton, 1, 1);
    result.add(removeButton, 2, 1);
    return result;
  }

  private void prepareTableView() {
    @NotNull String tableId = getTableId();
    setRowFactory();
    tableView.setId(tableId);
    prepareColumns();
    setSelectedItemListener();
    setSelectedIndexListener();
  }

  private void setRowFactory() {
    tableView.setRowFactory(tv -> {
      TableRow<EntityType> tableRow = new TableRow<>();
      tableRow.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
          runEditButtonHandler();
        }
      });
      return tableRow;
    });
  }

  private void setSelectedItemListener() {
    @Nullable TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    @Nullable ReadOnlyObjectProperty<EntityType> selectedItemProperty =
        tableViewSelectionModel.selectedItemProperty();
    if (selectedItemProperty == null) {
      throw new IluvatarRuntimeException("JFXListView: selectedItemProperty is null");
    }
    ChangeListener<EntityType> selectedItemListener = prepareSelectedItemListener();
    selectedItemProperty.addListener(selectedItemListener);
  }

  @NotNull
  private ChangeListener<EntityType> prepareSelectedItemListener() {
    return (observableValue, oldValue, newValue) -> {
      selectedEntitySetter.accept(newValue);
      if (newValue != null) {
        editButton.setDisable(false);
        removeButton.setDisable(false);
      } else {
        editButton.setDisable(true);
        removeButton.setDisable(true);
      }
    };
  }

  private void setSelectedIndexListener() {
    @Nullable TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    @Nullable ReadOnlyIntegerProperty selectedIndexProperty =
        tableViewSelectionModel.selectedIndexProperty();
    if (selectedIndexProperty == null) {
      throw new IluvatarRuntimeException("JFXListView: selectedIndexProperty is null");
    }
    selectedIndexProperty.addListener(
        (observableValue, oldValue, newValue) -> {
          selectedIndexSetter.accept((Integer) newValue);
        });
  }

  @NotNull
  private TableViewSelectionModel<EntityType> getTableViewSelectionModel() {
    @Nullable TableViewSelectionModel<EntityType> result = tableView.getSelectionModel();
    if (result == null) {
      throw new IluvatarRuntimeException("JFXListView: selectionModel is null");
    }
    return result;
  }

  private int getTableViewItemsCount() {
    @Nullable ObservableList<EntityType> itemList = tableView.getItems();
    if (itemList == null) {
      throw new IluvatarRuntimeException("JFXListView: itemList is null");
    }
    return itemList.size();
  }

  private void prepareColumns() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> columnList = prepareColumnList();
    @NotNull Iterator<Entry<String, ColumnInfo>> iterator = listViewInfo.getIterator();
    while (iterator.hasNext()) {
      @Nullable Entry<String, ColumnInfo> entry = iterator.next();
      if (entry != null) {
        @Nullable String fieldName = entry.getKey();
        @Nullable ColumnInfo columnInfo = entry.getValue();
        if ((fieldName != null) && (columnInfo != null)) {
          @NotNull TableColumn<EntityType, Object> tableColumn =
              new JFXTableColumn<>(columnInfo, fieldName);
          tableColumn.setSortable(true);
          columnList.add(tableColumn);
        }
      }
    }
  }

  @NotNull
  private ObservableList<TableColumn<EntityType, ?>> prepareColumnList() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> result =
        tableView.getColumns();
    if (result == null) {
      throw new IluvatarRuntimeException("JFXListView: columnList is null");
    }
    return result;
  }

  @NotNull
  private ObservableList<TableColumn<EntityType, ?>> prepareSortOrder() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> result =
        tableView.getSortOrder();
    if (result == null) {
      throw new IluvatarRuntimeException("JFXListView: sortOrder is null");
    }
    return result;
  }
}
