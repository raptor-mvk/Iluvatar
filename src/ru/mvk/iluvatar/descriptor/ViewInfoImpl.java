/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.NamedFieldInfo;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ViewInfoImpl<EntityType> implements ViewInfo<EntityType> {
	@NotNull
	private final Map<String, NamedFieldInfo> fields;
	@NotNull
	private final Class<EntityType> entityType;

	public ViewInfoImpl(@NotNull Class<EntityType> entityType) {
		this.entityType = entityType;
		fields = new LinkedHashMap<>();
	}

	@NotNull
	@Override
	public Class<EntityType> getEntityType() {
		return entityType;
	}

	@Override
	public int getFieldsCount() {
		return fields.size();
	}

	@NotNull
	@Override
	public Iterator<Entry<String, NamedFieldInfo>> getIterator() {
		return fields.entrySet().iterator();
	}

	@NotNull
	@Override
	public NamedFieldInfo getFieldInfo(@NotNull String fieldKey) {
		@Nullable NamedFieldInfo result = fields.get(fieldKey);
		/* TODO: push null through? */
		if (result == null) {
			throw new IluvatarRuntimeException("ViewInfoImpl: no field with key '" +
					fieldKey + "'");
		}
		return result;
	}

	@Override
	public void addFieldInfo(@NotNull String fieldKey, @NotNull NamedFieldInfo fieldInfo) {
		fields.put(fieldKey, fieldInfo);
	}
}
