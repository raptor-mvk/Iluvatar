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
  private final ListFieldInfo<Type, RefType> refFieldInfo;

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
      @Nullable RefType value = getValue();
      if (value == null) {
        fieldUpdater.accept(null);
      } else {
        @NotNull Serializable id = value.getId();
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
    @Nullable RefType item = refFieldInfo.getFinder().apply((Serializable) value);
    setValue(item);
  }

  @Override
  public void reload() {
    @NotNull List<RefType> itemsList = refFieldInfo.getListSupplier().get();
    @NotNull ObservableList<RefType> itemsObservableList =
        FXCollections.observableList(itemsList);
    setItems(itemsObservableList);
  }
}
