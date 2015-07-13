/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.module.db;

import org.hibernate.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SQLiteAbstractDbController implements DbController {
  @NotNull
  private final HibernateAdapter hibernateAdapter;

  protected SQLiteAbstractDbController(@NotNull HibernateAdapter hibernateAdapter) {
    this.hibernateAdapter = hibernateAdapter;
  }

  @Override
  public final boolean isDbSuitable() {
    @Nullable Object appId = getValue("pragma application_id;");
    return (appId instanceof Integer) && (int) appId == getAppId();
  }

  @Override
  public final int getDbVersion() {
    @Nullable Object dbVersion = getValue("pragma user_version;");
    if (!(dbVersion instanceof Integer)) {
      throw new IluvatarRuntimeException("SQLiteAbstractDbController: user_version " +
                                             "has wrong type");
    }
    return (int) dbVersion;
  }

  @Override
  public boolean updateDb() {
    int appDbVersion = getAppDbVersion();
    int dbVersion = getDbVersion();
    @Nullable Boolean result;
    if (dbVersion <= appDbVersion) {
      result = hibernateAdapter.executeInTransaction((session) -> {
        @NotNull Query query =
            hibernateAdapter.prepareSqlQuery("pragma user_version=" + appDbVersion + ';',
                                                session);
        return (query.executeUpdate() == 0);
      });
    } else {
      result = false;
    }
    if (result == null) {
      result = false;
    }
    return result && updateDbSchema(dbVersion);
  }

  @Override
  public boolean createDb() {
    long appId = getAppId();
    long appDbVersion = getAppDbVersion();
    @Nullable Boolean result = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query idQuery =
          hibernateAdapter.prepareSqlQuery("pragma application_id=" + appId + ';',
                                              session);
      @NotNull Query dbVersionQuery =
          hibernateAdapter.prepareSqlQuery("pragma user_version=" + appDbVersion + ';',
                                              session);
      return (idQuery.executeUpdate() == 0) && (dbVersionQuery.executeUpdate() == 0);
    });
    if (result == null) {
      result = false;
    }
    return result && createDbSchema();
  }

  protected void execute(@NotNull String sql) {
    hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      return query.executeUpdate();
    });
  }

  protected void executeList(@NotNull List<String> sqlList) {
    hibernateAdapter.executeInTransaction((session) -> {
      int result = 0;
      for (String sql : sqlList) {
        @NotNull Query query = hibernateAdapter.prepareSqlQuery
                                                    (sql, session);
        result += query.executeUpdate();
      }
      return result;
    });
  }

  protected void executeArray(@NotNull String[] sqlList) {
    executeList(Arrays.asList(sqlList));
  }

  @NotNull
  protected List<?> executeQuery(@NotNull String sql) {
    @Nullable List<?> result = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      return query.list();
    });
    if (result == null) {
      throw new IluvatarRuntimeException("Query result is null");
    }
    return result;
  }

  @Nullable
  private Object getValue(@NotNull String sql) {
    @Nullable Object value = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      @Nullable List<?> queryResult = query.list();
      if (queryResult == null) {
        throw new IluvatarRuntimeException("SQLiteAbstractDbController: list() " +
                                               "returned null");
      }
      return queryResult.get(0);
    });
    return value;
  }

  protected abstract boolean updateDbSchema(int fromDbVersion);

  protected abstract boolean createDbSchema();

  protected abstract int getAppId();

  protected abstract int getAppDbVersion();
}
