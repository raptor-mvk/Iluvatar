/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;

import java.util.Iterator;
import java.util.Map.Entry;


public interface ViewInfo<EntityType> {
	@NotNull
	Class<EntityType> getEntityType();

	int getFieldsCount();

	@NotNull
	Iterator<Entry<String, NamedFieldInfo>> getIterator();

	@NotNull
	NamedFieldInfo getFieldInfo(@NotNull String fieldKey);

	void addFieldInfo(@NotNull String field, @NotNull NamedFieldInfo fieldInfo);
}
