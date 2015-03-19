/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;
import ru.mvk.iluvatar.descriptor.column.ViewFormatter;

class JFXTableColumn<EntityType, CellType>
    extends TableColumn<EntityType, CellType> {
  @NotNull
  private final ColumnInfo columnInfo;
  @NotNull
  private final String columnKey;

  JFXTableColumn(@NotNull String columnName, @NotNull ColumnInfo columnInfo,
                 @NotNull String columnKey) {
    super(columnName);
    this.columnInfo = columnInfo;
    this.columnKey = columnKey;
    setCellValueFactory(new PropertyValueFactory<>(columnKey));
    setWidths();
    setCellFactory();
  }

  @NotNull
  String getColumnKey() {
    return columnKey;
  }

  private void setWidths() {
    // 0.7 is a ratio of conversion from font size to average letter width
    @NotNull Font defaultFont = Font.getDefault();
    double width = columnInfo.getWidth() * defaultFont.getSize() * 0.7;
    setMinWidth(width);
    setPrefWidth(width);
    setMaxWidth(width);
  }

  private void setCellFactory() {
    setCellFactory(param -> new TableCell<EntityType, CellType>() {
      @Override
      public void updateItem(@Nullable Object item, boolean empty) {
        @NotNull ViewFormatter viewFormatter = columnInfo.getViewFormatter();
        @NotNull String value = viewFormatter.apply(item);
        setText(value);
      }
    });
  }
}
