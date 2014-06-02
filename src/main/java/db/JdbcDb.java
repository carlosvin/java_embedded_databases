package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import domain.Price;

public class JdbcDb implements DB {

	enum Column {
		INSTRUMENT,
		MARKET,
		PRICE,
		DATE;
	}

	public static final String TABLE_NAME = "PRICES";
	public static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + Column.INSTRUMENT + " VARCHAR(30), "
			+ Column.MARKET + " VARCHAR(30), " + Column.PRICE + " FLOAT, " + Column.DATE
			+ " TIMESTAMP,   primary key (" + Column.INSTRUMENT + ", " + Column.MARKET + ", " + Column.DATE + "))";
	public static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME
			+ " (INSTRUMENT, MARKET, PRICE, DATE) VALUES (?,?,?,?)";

	public static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Column.PRICE + " = ? WHERE "
			+ Column.INSTRUMENT + " = ?";
	public static final String DATABASE_NAME = "dbs/myjavadbtest";
	protected final Connection conn;

	public JdbcDb(String driver, String url) throws Exception {
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(url);
		conn.setAutoCommit(false);
	}

	private void insert(Price p, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, p.getInstrument());
		preparedStatement.setString(2, p.getMarket());
		preparedStatement.setDouble(3, p.getPrice());
		preparedStatement.setTimestamp(4, new Timestamp(p.getDate().getTime()));
		preparedStatement.addBatch();
	}

	@Override
	public void insert(Price... prices) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
		for (Price p : prices) {
			insert(p, preparedStatement);
		}
		preparedStatement.executeBatch();
		preparedStatement.close();
		conn.commit();
	}

	@Override
	public void createTable() {
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate(SQL_CREATE);
			stm.close();
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + TABLE_NAME);
			stm.close();
			conn.commit();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	@Override
	public Set<Price> selectAll() {
		Set<Price> pricesOut = new HashSet<>();
		try {
			Statement stm = conn.createStatement();
			ResultSet resultSet = stm.executeQuery(SQL_SELECT_ALL);
			while (resultSet.next()) {
				Price price = new Price(resultSet.getString(Column.MARKET.toString()),
						resultSet.getString(Column.INSTRUMENT.toString()));
				price.setDate(resultSet.getTimestamp(Column.DATE.toString()).getTime());
				price.setPrice(resultSet.getDouble(Column.PRICE.toString()));
				pricesOut.add(price);
			}
			stm.close();

		} catch (SQLException e) {
			System.err.println(e);
		}
		return pricesOut;
	}

	@Override
	public void update(Price... prices) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
		for (Price p : prices) {
			update(p, preparedStatement);
		}
		preparedStatement.executeBatch();
		preparedStatement.close();
		conn.commit();
	}

	private void update(Price p, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setDouble(1, p.getPrice());
		preparedStatement.setString(2, p.getInstrument());
		preparedStatement.addBatch();
	}
}
