/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx.field;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.ListFieldInfo;
import ru.mvk.iluvatar.descriptor.field.RefAble;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.utils.IluvatarUtils;

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
  @NotNull
  private String prefix;

  public RefField(@NotNull ListFieldInfo<Type, RefType> refFieldInfo) {
    int width = refFieldInfo.getWidth();
    prefix = "";
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
    prepareEscapeHandler();
    prepareFocusHandler();
    prepareKeyHandler();
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
      if (value != null) {
        @NotNull Serializable id = value.getId();
        @NotNull Type typedId = refFieldInfo.getType().cast(id);
        fieldUpdater.accept(typedId);
      }
    });
  }

  private void prepareEscapeHandler() {
    addEventFilter(KeyEvent.KEY_RELEASED, (event) -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        prefix = "";
        selectItemByPrefix();
        event.consume();
      }
    });
  }

  private void prepareFocusHandler() {
    @Nullable ReadOnlyBooleanProperty focused = focusedProperty();
    if (focused == null) {
      throw new IluvatarRuntimeException("RefField: focused property is null");
    }
    focused.addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        prefix = "";
      }
    });
  }

  private void prepareKeyHandler() {
    setOnKeyReleased((event) -> {
      if (!(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB ||
                event.getCode() == KeyCode.ESCAPE)) {
        int prefixLength = prefix.length();
        if (event.getCode() == KeyCode.BACK_SPACE && prefixLength > 0) {
          prefix = prefix.substring(0, prefixLength - 1);
          selectItemByPrefix();
        } else {
          @Nullable String text = event.getText();
          if (text != null && !text.isEmpty()) {
            prefix += IluvatarUtils.normalizeString(event.getText());
            selectItemByPrefix();
          }
        }
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
    FXCollections.sort(itemsObservableList, new RefComparator<>());
    setItems(itemsObservableList);
  }

  private void selectItemByPrefix() {
    @NotNull List<RefType> itemsList = getItems();
    int count = itemsList.size();
    int result = -1;
    if (count > 0) {
      for (int i = 0; i < count && result < 0; i++) {
        @NotNull String normalized =
            IluvatarUtils.normalizeString(itemsList.get(i).toString());
        if (prefix.compareTo(normalized) <= 0) {
          result = i;
        }
      }
      if (result < 0) {
        result = count - 1;
      }
    } else {
      result = 0;
    }
    setValue(itemsList.get(result));
  }
}
