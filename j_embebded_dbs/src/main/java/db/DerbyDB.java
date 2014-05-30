package db;

public class DerbyDB extends JdbcDb {

	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String URL_PREFIX = "jdbc:derby:";
	public static final String URL_SUFIX = ";create=true;user=me;password=mine";

	public DerbyDB(String folderPath) throws Exception {
		super(DRIVER, URL_PREFIX + folderPath + "/derby.db" + URL_SUFIX);
	}

}
