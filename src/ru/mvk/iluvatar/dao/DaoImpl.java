/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.utils.IluvatarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* TODO: unit tests */
public class DaoImpl<EntityType, PrimaryKeyType extends Serializable>
		implements Dao<EntityType, PrimaryKeyType> {
	@NotNull
	private final Class<EntityType> entityType;
	@NotNull
	private final Class<PrimaryKeyType> primaryKeyType;
	@NotNull
	private final HibernateAdapter hibernateAdapter;

	public DaoImpl(@NotNull Class<EntityType> entityType,
	               @NotNull Class<PrimaryKeyType> primaryKeyType,
	               @NotNull HibernateAdapter hibernateAdapter) {
		this.entityType = entityType;
		this.primaryKeyType = primaryKeyType;
		this.hibernateAdapter = hibernateAdapter;
	}

	@Override
	@NotNull
	public Class<EntityType> getEntityType() {
		return entityType;
	}

	@NotNull
	@Override
	public PrimaryKeyType create(@NotNull EntityType entity) {
		@Nullable Serializable primaryKey =
				executeInTransaction((session) -> session.save(entity));
		return castPrimaryKey(primaryKey);
	}

	@Nullable
	@Override
	public EntityType read(@NotNull Serializable id) {
		@NotNull Class<EntityType> entityType = getEntityType();
		@Nullable Object result =
				executeInTransaction((session) -> session.get(entityType, id));
		return entityType.cast(result);
	}

	@Override
	public void update(@NotNull EntityType entity) {
		executeInTransaction((session) -> {
			session.update(entity);
			return true;
		});
	}

	@Override
	public void delete(@NotNull EntityType entity) {
		executeInTransaction((session) -> {
			session.delete(entity);
			return true;
		});
	}

	@NotNull
	@Override
	public List<EntityType> list() {
		@NotNull List<EntityType> result;
		@NotNull Class<EntityType> entityType = getEntityType();
		@Nullable List<?> entityList = executeInTransaction((session) -> {
			@NotNull Criteria criteria = session.createCriteria(entityType);
			return (List<?>) criteria.list();
		});
		if (entityList == null) {
			result = new ArrayList<>();
		} else {
			@NotNull Stream<EntityType> entityStream =
					IluvatarUtils.mapToTypedStream(entityList, entityType);
			result = entityStream.collect(Collectors.toList());
		}
		return result;
	}

	@NotNull
	@Override
	public List<EntityType> orderedList(@NotNull String field, boolean isAscending) {
		@NotNull List<EntityType> result;
		@NotNull Class<EntityType> entityType = getEntityType();
		@Nullable List<?> entityList = executeInTransaction((session) -> {
			@NotNull Criteria criteria = session.createCriteria(entityType);
			@NotNull Order order;
			if (isAscending) {
				order = Order.asc(field);
			} else {
				order = Order.desc(field);
			}
			criteria.addOrder(order);
			return (List<?>) criteria.list();
		});
		if (entityList == null) {
			result = new ArrayList<>();
		} else {
			@NotNull Stream<EntityType> entityStream =
					IluvatarUtils.mapToTypedStream(entityList, entityType);
			result = entityStream.collect(Collectors.toList());
		}
		return result;
	}

	@NotNull
	private PrimaryKeyType castPrimaryKey(@Nullable Serializable id) {
		@NotNull Class<PrimaryKeyType> primaryKeyType = getPrimaryKeyType();
		@Nullable PrimaryKeyType result = primaryKeyType.cast(id);
		/* TODO: may be send null through? */
		if (result == null) {
			throw new IluvatarRuntimeException("DaoImpl: create result is null");
		}
		return result;
	}

	@Override
	@NotNull
	public Class<PrimaryKeyType> getPrimaryKeyType() {
		return primaryKeyType;
	}

	@Nullable
	private <Type> Type executeInTransaction(@NotNull Function<Session, Type> function) {
		return hibernateAdapter.executeInTransaction(function);
	}
}
