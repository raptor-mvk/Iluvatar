/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.column.ColumnInfo;

class JFXTableLastColumn<EntityType, CellType>
		extends JFXTableColumn<EntityType, CellType> {
	JFXTableLastColumn(@NotNull String columnName, @NotNull ColumnInfo columnInfo,
	                   @NotNull String columnKey) {
		super(columnName, columnInfo, columnKey);
	}

	@Override
	String getStringSuffix() {
		return "    ";
	}
}
