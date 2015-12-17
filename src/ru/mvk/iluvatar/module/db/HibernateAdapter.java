/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.module.db;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface HibernateAdapter {
	@NotNull
	Query prepareSqlQuery(@NotNull String sql, @NotNull Session session);

	@Nullable
	<Type> Type executeInTransaction(@NotNull Function<Session, Type> function);
}
