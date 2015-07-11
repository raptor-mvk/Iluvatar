/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.ListFieldInfo;
import ru.mvk.iluvatar.descriptor.field.RefAble;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class RefField<Type extends Serializable, RefType extends RefAble>
    extends ComboBox<RefType> implements Field<Type>, RefList {
  @NotNull
  private Consumer<Type> fieldUpdater = (value) -> {
  };
  @NotNull
  ListFieldInfo<Type, RefType> refFieldInfo;

  public RefField(@NotNull ListFieldInfo<Type, RefType> refFieldInfo) {
    int width = refFieldInfo.getWidth();
    if (width <= 0) {
      throw new IluvatarRuntimeException("RefField: non-positive width");
    }
    this.refFieldInfo = refFieldInfo;
    // force double length for too short fields
    if (width > 5) {
      setAllWidths(width);
    } else {
      setAllWidths(width * 2);
    }
    prepareActionHandler();
  }

  private void setAllWidths(int length) {
    // 0.75 is a ratio of conversion from font size to average letter width
    Font defaultFont = Font.getDefault();
    double width = length * defaultFont.getSize() * 0.75;
    setMinWidth(width);
    setMaxWidth(width);
    setMaxWidth(width);
  }

  private void prepareActionHandler() {
    setOnAction((event) -> {
      @NotNull SingleSelectionModel<RefType> selectionModel = prepareSelectionModel();
      @Nullable RefType selected = selectionModel.getSelectedItem();
      if (selected == null) {
        fieldUpdater.accept(null);
      } else {
        @NotNull Serializable id = selected.getId();
        @NotNull Type typedId = refFieldInfo.getType().cast(id);
        fieldUpdater.accept(typedId);
      }
    });
  }

  @Override
  public void setFieldUpdater(@NotNull Consumer<Type> fieldUpdater) {
    this.fieldUpdater = fieldUpdater;
  }

  @Override
  public void setFieldValue(@NotNull Object value) {
    if (!(value instanceof Serializable)) {
      throw new IluvatarRuntimeException("RefField: wrong value type");
    }
    @NotNull SingleSelectionModel<RefType> selectionModel = prepareSelectionModel();
    @Nullable RefType item;
    item = refFieldInfo.getFinder().apply((Serializable) value);
    selectionModel.select(item);
  }

  @Override
  public void reload() {
    @NotNull List<RefType> itemsList = refFieldInfo.getListSupplier().get();
    @NotNull ObservableList<RefType> itemsObservableList =
        FXCollections.observableList(itemsList);
    setItems(itemsObservableList);
  }

  @NotNull
  private SingleSelectionModel<RefType> prepareSelectionModel() {
    @Nullable SingleSelectionModel<RefType> result = getSelectionModel();
    if (result == null) {
      throw new IluvatarRuntimeException("RefField: selectionModel is null");
    }
    return result;
  }
}
