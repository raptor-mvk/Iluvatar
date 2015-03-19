/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.HibernateAdapterImpl;
import ru.mvk.iluvatar.utils.PowerMockUtils;

public class DaoImplUnitTests {
  @Test
  public void constructor_ShouldSetEntityType() {
    @NotNull Class<Object> expectedEntityType = Object.class;
    @NotNull Session session = PowerMockUtils.mock(Session.class);
    @NotNull SessionFactory sessionFactory = prepareSessionFactory(session);
    @NotNull HibernateAdapter hibernateAdapter = new HibernateAdapterImpl(sessionFactory);
    @NotNull Dao<Object, Integer> dao = new DaoImpl<>(expectedEntityType,
        Integer.class, hibernateAdapter);
    @NotNull Class<?> entityType = dao.getEntityType();
    Assert.assertEquals("constructor should set correct value of 'entityType'",
        expectedEntityType, entityType);
  }

  @NotNull
  private SessionFactory prepareSessionFactory(@NotNull Session session) {
    @NotNull SessionFactory sessionFactory = PowerMockUtils.mock(SessionFactory.class);
    PowerMockUtils.when(sessionFactory.getCurrentSession()).thenReturn(session);
    return sessionFactory;
  }
}
