/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.dao;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.field.ListAdapter;
import ru.mvk.iluvatar.descriptor.field.RefAble;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/* TODO test */
public class DaoListAdapter<EntityType extends RefAble,
		PrimaryKeyType extends Serializable>
		implements ListAdapter<PrimaryKeyType, EntityType> {
	@NotNull
	private final Dao<EntityType, PrimaryKeyType> dao;

	public DaoListAdapter(@NotNull Dao<EntityType, PrimaryKeyType> dao) {
		this.dao = dao;
	}

	@NotNull
	@Override
	public Class<PrimaryKeyType> getType() {
		return dao.getPrimaryKeyType();
	}

	@NotNull
	@Override
	public Class<EntityType> getRefType() {
		return dao.getEntityType();
	}

	@NotNull
	@Override
	public Supplier<List<EntityType>> getListSupplier() {
		return dao::list;
	}

	@NotNull
	@Override
	public Function<Serializable, EntityType> getFinder() {
		return (id) -> {
			@NotNull PrimaryKeyType typedId = dao.getPrimaryKeyType().cast(id);
			return dao.read(typedId);
		};
	}
}
