/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.module.db;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.function.Function;

public class HibernateAdapterImpl implements HibernateAdapter {
  @NotNull
  private final SessionFactory sessionFactory;

  public HibernateAdapterImpl(@NotNull SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @NotNull
  @Override
  public Query prepareSqlQuery(@NotNull String sql, @NotNull Session session) {
    @Nullable Query query = session.createSQLQuery(sql);
    if (query == null) {
      throw new IluvatarRuntimeException("HibernateAdapterImpl: query is null");
    }
    return query;
  }

  @Nullable
  @Override
  public <Type> Type executeInTransaction(@NotNull Function<Session, Type> function) {
    @Nullable Type result = null;
    @NotNull Session session = getCurrentSession();
    @NotNull Transaction transaction = beginTransaction(session);
    try {
      result = function.apply(session);
      transaction.commit();
    } catch (Throwable e) {
      transaction.rollback();
    }
    return result;
  }

  @NotNull
  private Session getCurrentSession() {
    @Nullable Session result = sessionFactory.getCurrentSession();
    if (result == null) {
      throw new IluvatarRuntimeException("HibernateAdapterImpl: currentSession is " +
                                             "null");
    }
    return result;
  }

  @NotNull
  private Transaction beginTransaction(@NotNull Session session) {
    @Nullable Transaction transaction = session.beginTransaction();
    if (transaction == null) {
      throw new IluvatarRuntimeException("HibernateAdapterImpl: transaction is null");
    }
    return transaction;
  }

}
