package db;

import java.sql.SQLException;
import java.util.Set;

import domain.Price;

public interface DB {

	public void insert(Price... prices) throws SQLException;

	public void createTable();

	public void deleteAll();

	public void update(Price... prices) throws SQLException;

	public Set<Price> selectAll();

}
