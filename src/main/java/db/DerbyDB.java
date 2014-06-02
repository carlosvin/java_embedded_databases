package db;

import java.sql.SQLException;
import java.sql.Statement;

public class DerbyDB extends JdbcDb {

	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String URL_PREFIX = "jdbc:derby:";
	public static final String SCHEMA = "me";
	public static final String URL_SUFIX = ";create=true;user=" + SCHEMA + ";password=mine;";
	public static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA " + SCHEMA;

	public DerbyDB(String folderPath) throws Exception {
		super(DRIVER, URL_PREFIX + folderPath + "/derby.db" + URL_SUFIX);
		createSchema();
	}

	private void createSchema() {
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate(CREATE_SCHEMA_QUERY);
			stm.close();
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
