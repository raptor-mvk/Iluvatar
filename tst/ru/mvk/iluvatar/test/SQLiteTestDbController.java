/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.test;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.SQLiteAbstractDbController;

public class SQLiteTestDbController extends SQLiteAbstractDbController {
  private final int appId;
  private final int appDbVersion;

  public SQLiteTestDbController(@NotNull HibernateAdapter hibernateAdapter, int appId,
                                int appDbVersion) {
    super(hibernateAdapter);
    this.appId = appId;
    this.appDbVersion = appDbVersion;
  }

  @Override
  protected boolean updateDbSchema(int fromDbVersion) {
    return true;
  }

  @Override
  protected boolean createDbSchema() {
    return true;
  }

  @Override
  protected int getAppId() {
    return appId;
  }

  @Override
  protected int getAppDbVersion() {
    return appDbVersion;
  }
}
