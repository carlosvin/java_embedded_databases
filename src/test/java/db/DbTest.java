package db;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Price;

public class DbTest {

	private static final int MAX_PRICES = 1;
	private static final String FOLDER = "dbs";
	private final Set<Price> pricesIn = new HashSet<Price>();
	private final Price[] pricesInUpdate = new Price[MAX_PRICES];
	private static Path tempDir;

	@BeforeClass
	public static void createFolder() {
		try {
			tempDir = Files.createDirectory(new File(FOLDER).toPath());
		} catch (IOException e) {
			tempDir = new File(FOLDER).toPath();
		}
	}

	@Before
	public void setup() {
		if (pricesIn.isEmpty()) {
			for (int i = 0; i < MAX_PRICES; i++) {
				pricesIn.add(Price.create(i));
				pricesInUpdate[i] = Price.create(i);
				pricesInUpdate[i].setPrice(pricesInUpdate[i].getPrice() + 1);
			}
			System.out.println("Testing with " + MAX_PRICES + " elements");
		}
	}

	@Test
	public void testObjectDB() throws SQLException {
		preformanceTest(new ObjectDB(FOLDER));
	}

	@Test
	public void testSqliteDB() throws SQLException, Exception {
		preformanceTest(new SqliteDB(FOLDER));
	}

	@Test
	public void testDerbyDB() throws SQLException, Exception {
		preformanceTest(new DerbyDB(FOLDER));
	}

	private void preformanceTest(DB db) throws SQLException {
		db.createTable();
		long startInsert = System.currentTimeMillis();
		db.insert(pricesIn.toArray(new Price[1]));
		long endInsert = System.currentTimeMillis();
		Collection<Price> pricesOut = db.selectAll();
		long endSelect = System.currentTimeMillis();
		db.update(pricesInUpdate);
		long endUpdate = System.currentTimeMillis();
		db.deleteAll();
		long endDelete = System.currentTimeMillis();

		System.out.println(pricesOut.size() + " Prices " + db.getClass().getSimpleName() + ", total time "
				+ (endDelete - startInsert) + " ms");
		System.out.println("\tInsert:\t " + (endInsert - startInsert) + " ms");
		System.out.println("\tSelect:\t " + (endSelect - endInsert) + " ms");
		System.out.println("\tUpdate:\t " + (endUpdate - endSelect) + " ms");
		System.out.println("\tDelete:\t " + (endDelete - endUpdate) + " ms");
		System.out.println("----------------------------------------------");

		assertEquals(pricesIn, pricesOut);
	}
}
