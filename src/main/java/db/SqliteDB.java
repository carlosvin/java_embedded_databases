package db;


public class SqliteDB extends JdbcDb {

	public static final String DRIVER = "org.sqlite.JDBC";
	public static final String DATABASE_NAME = "dbs/mysqlitetest";
	public static final String URL_PREFIX = "jdbc:sqlite:";

	public SqliteDB(String folderPath) throws Exception {
		super(DRIVER, URL_PREFIX + folderPath + "/prices.sqlite");
	}
}
