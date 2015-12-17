/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.integration;

import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.dao.Dao;
import ru.mvk.iluvatar.dao.DaoImpl;
import ru.mvk.iluvatar.module.db.DbController;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.HibernateAdapterImpl;
import ru.mvk.iluvatar.test.SQLiteTestDbController;
import ru.mvk.iluvatar.test.TestObject;
import ru.mvk.iluvatar.utils.SQLiteHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbIntegrationTests {
	@NotNull
	private final String DB_FILENAME = "db.sqlite";
	private final int APP_ID = 0x002d2514;
	private final int APP_DB_VERSION = 3;
	@NotNull
	private final SQLiteHelper sqLiteHelper = new SQLiteHelper();
	@NotNull
	private final SessionFactory sessionFactory =
			sqLiteHelper.prepareSessionFactory(DB_FILENAME);
	@NotNull
	private final HibernateAdapter hibernateAdapter =
			new HibernateAdapterImpl(sessionFactory);

	@Test
	public void daoImplCreate_ShouldCreateDatabaseEntry() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			@NotNull String expectedName = "Test";
			prepareDb();
			@NotNull TestObject expectedTestObject = new TestObject(expectedName);
			@NotNull Long id = dao.create(expectedTestObject);
			@Nullable String name = getTestObjectName(id);
			Assert.assertEquals("create() should create database entry", expectedName,
					name);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplRead_ShouldReturnDatabaseEntry() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			@NotNull String expectedName = "Test";
			@NotNull long expectedId = 4;
			@NotNull TestObject expectedTestObject = new TestObject(expectedId, expectedName);
			prepareDb();
			putTestObject(expectedTestObject);
			@Nullable TestObject testObject = dao.read(expectedId);
			Assert.assertEquals("read() should return database entry", expectedTestObject,
					testObject);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplRead_WrongId_ShouldReturnNull() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			prepareDb();
			@Nullable TestObject testObject = dao.read(3L);
			Assert.assertNull("read() should return null, when no entry found", testObject);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplUpdate_ShouldUpdateDatabaseEntry() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			long expectedId = 2L;
			@NotNull String expectedName = "result";
			@NotNull TestObject expectedTestObject = new TestObject(expectedId, expectedName);
			prepareDbWithTestObject(expectedId);
			dao.update(expectedTestObject);
			@Nullable String name = getTestObjectName(expectedId);
			Assert.assertEquals("update() should update database entry", expectedName, name);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplUpdate_NoEntry_ShouldNotCreateDatabaseEntry() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			long expectedId = 2L;
			@NotNull TestObject expectedTestObject = new TestObject(expectedId, "wrong");
			prepareDb();
			dao.update(expectedTestObject);
			@Nullable String name = getTestObjectName(expectedId);
			Assert.assertNull("update() should not create database entry for new entity",
					name);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplDelete_ShouldDeleteDatabaseEntry() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			long expectedId = 2L;
			@NotNull TestObject expectedTestObject = new TestObject(expectedId, "entity");
			prepareDbWithTestObject(expectedId);
			dao.delete(expectedTestObject);
			@Nullable String name = getTestObjectName(expectedId);
			Assert.assertNull("delete() should delete database entry", name);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplDelete_WrongEntity_ShouldNotThrowException() {
		boolean exceptionCaught = false;
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			long expectedId = 2L;
			@NotNull TestObject expectedTestObject = new TestObject(expectedId, "entity");
			prepareDb();
			dao.delete(expectedTestObject);
		} catch (Throwable e) {
			exceptionCaught = true;
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
		Assert.assertFalse("delete() should not throw any exception, when entity was not " +
				"found in the database", exceptionCaught);
	}

	@Test
	public void daoImplList_ShouldReturnAllEntries() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			@NotNull List<TestObject> expectedList = prepareList();
			prepareDbWithTestObjectList(expectedList);
			@NotNull List<TestObject> list = dao.list();
			boolean listsAreEqual =
					expectedList.size() == list.size() && expectedList.containsAll(list) &&
							list.containsAll(expectedList);
			Assert.assertTrue("list() should return all database entries", listsAreEqual);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplList_NameFalse_ShouldReturnAllEntriesSortedInDescendingOrderById() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			@NotNull List<TestObject> expectedList = prepareList();
			prepareDbWithTestObjectList(expectedList);
			@NotNull List<TestObject> list = dao.orderedList("name", false);
			boolean listsAreEqual =
					expectedList.size() == list.size() && expectedList.containsAll(list) &&
							list.containsAll(expectedList);
			for (int i = 1, count = list.size(); i < count; i++) {
				@NotNull String previousTestObjectName = list.get(i - 1).getName();
				@NotNull String currentTestObjectName = list.get(i).getName();
				listsAreEqual = listsAreEqual &&
						previousTestObjectName.compareTo(currentTestObjectName) >= 0;
			}
			Assert.assertTrue("list() should return all database entries sorted in " +
					"ascending order by id", listsAreEqual);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void daoImplList_IdTrue_ShouldReturnAllEntriesSortedInAscendingOrderById() {
		try {
			@NotNull Dao<TestObject, Long> dao =
					new DaoImpl<>(TestObject.class, Long.class, hibernateAdapter);
			@NotNull List<TestObject> expectedList = prepareList();
			prepareDbWithTestObjectList(expectedList);
			@NotNull List<TestObject> list = dao.orderedList("id", true);
			boolean listsAreEqual =
					expectedList.size() == list.size() && expectedList.containsAll(list) &&
							list.containsAll(expectedList);
			for (int i = 1, count = list.size(); i < count; i++) {
				@NotNull TestObject previousTestObject = list.get(i - 1);
				@NotNull TestObject currentObject = list.get(i);
				listsAreEqual = listsAreEqual &&
						previousTestObject.getId() <= currentObject.getId();
			}
			Assert.assertTrue("list() should return all database entries sorted in " +
					"ascending order by id", listsAreEqual);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerIsDbSuitable_NoDb_ShouldReturnFalse() {
		@NotNull DbController dbController =
				new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
		try {
			boolean result = dbController.isDbSuitable();
			Assert.assertFalse("isDbSuitable() should return false, when appId is incorrect",
					result);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerIsDbSuitable_ShouldReturnTrueWhenAppIdIsCorrect() {
		@NotNull DbController dbController =
				new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
		prepareDbWithServiceInfo(APP_ID, APP_DB_VERSION);
		try {
			boolean result = dbController.isDbSuitable();
			Assert.assertTrue("isDbSuitable() should return true, when appId is correct",
					result);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerGetDbVersion_NoDb_ShouldReturnZero() {
		@NotNull DbController dbController =
				new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
		try {
			int dbVersion = dbController.getDbVersion();
			Assert.assertEquals("getDbVersion() should zero, when db is new", 0, dbVersion);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerGetDbVersion_ShouldReturnCorrectDbVersion() {
		@NotNull DbController dbController =
				new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
		prepareDbWithServiceInfo(APP_ID, APP_DB_VERSION);
		try {
			int dbVersion = dbController.getDbVersion();
			Assert.assertEquals("getDbVersion() should correct database version",
					APP_DB_VERSION, dbVersion);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test()
	public void dbControllerUpdateDb_TooLargeVersion_ShouldNotChangeDbVersion() {
		try {
			@NotNull DbController dbController =
					new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
			int expectedDbVersion = 5;
			prepareDbWithServiceInfo(APP_ID, expectedDbVersion);
			dbController.updateDb();
			int dbVersion = dbController.getDbVersion();
			Assert.assertEquals("updateDb() should not change dbVersion, when it is greater, " +
					"than appDbVersion", expectedDbVersion, dbVersion);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test()
	public void dbControllerUpdateDb_TooLargeVersion_ShouldReturnFalse() {
		try {
			@NotNull DbController dbController =
					new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
			int expectedDbVersion = 5;
			prepareDbWithServiceInfo(APP_ID, expectedDbVersion);
			boolean result = dbController.updateDb();
			Assert.assertFalse("updateDb() should return false, when dbVersion is greater, " +
					"than appDbVersion", result);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerUpdateDb_ShouldSetDbVersionValueToAppDbVersion() {
		try {
			@NotNull DbController dbController =
					new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
			prepareDbWithServiceInfo(APP_ID, 0);
			dbController.updateDb();
			int dbVersion = dbController.getDbVersion();
			Assert.assertEquals("updateDb() should set dbVersion value to appDbVersion",
					APP_DB_VERSION, dbVersion);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerCreateDb_ShouldSetDbVersionValueToAppDbVersion() {
		try {
			@NotNull DbController dbController =
					new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
			dbController.createDb();
			int dbVersion = dbController.getDbVersion();
			Assert.assertEquals("createDb() should set dbVersion value to appDbVersion",
					APP_DB_VERSION, dbVersion);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	@Test
	public void dbControllerCreateDb_ShouldSetAppIdValueToAppId() {
		try {
			@NotNull DbController dbController =
					new SQLiteTestDbController(hibernateAdapter, APP_ID, APP_DB_VERSION);
			dbController.createDb();
			boolean result = dbController.isDbSuitable();
			Assert.assertTrue("createDb() should set appId value to appId",
					result);
		} finally {
			sqLiteHelper.removeDbFile(sessionFactory, DB_FILENAME);
		}
	}

	private void prepareDb() {
		try {
			sqLiteHelper.connect(DB_FILENAME);
			sqLiteHelper.execute("create table object (name text);");
		} finally {
			sqLiteHelper.disconnect();
		}
	}

	private void prepareDbWithServiceInfo(int appId, int dbVersion) {
		try {
			sqLiteHelper.connect(DB_FILENAME);
			sqLiteHelper.execute("pragma application_id=" + appId + ';');
			sqLiteHelper.execute("pragma user_version=" + dbVersion + ';');
		} finally {
			sqLiteHelper.disconnect();
		}
	}

	private void prepareDbWithTestObject(long id) {
		prepareDb();
		@NotNull TestObject testObject = new TestObject(id, "testObject");
		putTestObject(testObject);
	}

	private void prepareDbWithTestObjectList(List<TestObject> list) {
		prepareDb();
		list.forEach(this::putTestObject);
	}

	@Nullable
	private String getTestObjectName(long id) {
		@Nullable String result = null;
		try {
			sqLiteHelper.connect(DB_FILENAME);
			@Nullable ResultSet resultSet =
					sqLiteHelper.executeQuery("select name from object where rowid = " + id + ";");
			if (resultSet != null && resultSet.next()) {
				result = resultSet.getString(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database entry");
		} finally {
			sqLiteHelper.disconnect();
		}
		return result;
	}

	private void putTestObject(@NotNull TestObject testObject) {
		try {
			sqLiteHelper.connect(DB_FILENAME);
			long id = testObject.getId();
			@NotNull String name = testObject.getName();
			sqLiteHelper.execute("insert into object (rowid, name) values (" + id + ", '" +
					name + "');");
		} finally {
			sqLiteHelper.disconnect();
		}
	}

	@NotNull
	private List<TestObject> prepareList() {
		@NotNull TestObject testObject = new TestObject(1L, "first");
		@NotNull TestObject testObject2 = new TestObject(2L, "second");
		@NotNull TestObject testObject3 = new TestObject(3L, "third");
		@NotNull List<TestObject> result = new ArrayList<>();
		result.add(testObject3);
		result.add(testObject);
		result.add(testObject2);
		return result;
	}
}
