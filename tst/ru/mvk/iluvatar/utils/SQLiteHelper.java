/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.test.TestObject;

import java.io.File;
import java.sql.*;
import java.util.Properties;

public class SQLiteHelper {
  @Nullable
  private Connection dbConnection;
  @Nullable
  private Statement statement;

  public void connect(@NotNull String dbFileName) {
    try {
      dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
      if (dbConnection == null) {
        throw new RuntimeException("Connection is null");
      }
      statement = dbConnection.createStatement();
    } catch (SQLException e) {
      throw new RuntimeException("Could not connect to database");
    }
  }

  @Nullable
  public ResultSet executeQuery(@NotNull String sql) {
    @Nullable ResultSet resultSet;
    if (statement == null) {
      throw new RuntimeException("Statement is null");
    }
    try {
      resultSet = statement.executeQuery(sql);
    } catch (SQLException e) {
      throw new RuntimeException("Could not execute query");
    }
    return resultSet;
  }

  public void execute(@NotNull String sql) {
    if (statement == null) {
      throw new RuntimeException("Statement is null");
    }
    try {
      statement.execute(sql);
    } catch (SQLException e) {
      throw new RuntimeException("Could not execute query");
    }
  }

  public void disconnect() {
    try {
      if (statement != null) {
        statement.close();
      }
      if (dbConnection != null) {
        dbConnection.close();
      }
    } catch (SQLException ignored) {
    }
  }

  @NotNull
  public SessionFactory prepareSessionFactory(@NotNull String dbFileName) {
    @NotNull Configuration configuration = prepareConfiguration(dbFileName);
    @NotNull Properties properties = configuration.getProperties();
    @NotNull ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                                   .applySettings(properties).build();
    return configuration.buildSessionFactory(serviceRegistry);
  }

  public void removeDbFile(@NotNull SessionFactory sessionFactory,
                           @NotNull String dbFileName) {
    sessionFactory.close();
    boolean result = new File(dbFileName).delete();
    if (!result) {
      throw new RuntimeException("Could not remove db file");
    }
  }

  @NotNull
  private Configuration prepareConfiguration(@NotNull String dbFileName) {
    @NotNull Configuration configuration = new Configuration();
    @NotNull Properties properties = new Properties();
    properties.put("hibernate.connection.driver_class", "org.sqlite.JDBC");
    properties.put("hibernate.connection.url", "jdbc:sqlite:" + dbFileName);
    properties.put("hibernate.current_session_context_class", "thread");
    properties.put("hibernate.dialect", "ru.mvk.service.hibernate.SQLiteDialect");
    configuration.setProperties(properties);
    configuration.addAnnotatedClass(TestObject.class);
    return configuration;
  }

}
