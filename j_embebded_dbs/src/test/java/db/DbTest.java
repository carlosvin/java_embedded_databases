package db;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import domain.Price;

public class DbTest {

	private static final int MAX_PRICES = 1;
	private static final String FOLDER = "dbs";
	private static Set<Price> pricesIn = new HashSet<Price>();

	@BeforeClass
	public static void initPrices() {
		for (int i = 0; i < MAX_PRICES; i++) {
			pricesIn.add(Price.create(i));
		}
	}

	@Test
	public void testObjectDB() throws SQLException {
		DbTest.preformanceTest(new ObjectDB(FOLDER), pricesIn);
	}

	@Test
	public void testSqliteDB() throws SQLException, Exception {
		DbTest.preformanceTest(new SqliteDB(FOLDER), pricesIn);
	}

	@Test
	public void testDerbyDB() throws SQLException, Exception {
		DbTest.preformanceTest(new DerbyDB(FOLDER), pricesIn);
	}

	private static void preformanceTest(DB db, Set<Price> pricesIn) throws SQLException {
		db.createTable();
		long startInsert = System.currentTimeMillis();
		db.insert(pricesIn.toArray(new Price[1]));
		long endInsert = System.currentTimeMillis();
		Collection<Price> pricesOut = db.selectAll();

		assertEquals(pricesIn, pricesOut);

		long endSelect = System.currentTimeMillis();
		db.update(pricesIn.toArray(new Price[1]));
		long endUpdate = System.currentTimeMillis();
		db.deleteAll();
		long endDelete = System.currentTimeMillis();

		// assertEquals(pricesIn, pricesOut);

		System.out.println(pricesOut.size() + " Prices " + db.getClass().getSimpleName() + ", total time "
				+ (endDelete - startInsert) + " ms");
		System.out.println("\tInsert:\t " + (endInsert - startInsert) + " ms");
		System.out.println("\tSelect:\t " + (endSelect - endInsert) + " ms");
		System.out.println("\tUpdate:\t " + (endUpdate - endSelect) + " ms");
		System.out.println("\tDelete:\t " + (endDelete - endUpdate) + " ms");
		System.out.println("----------------------------------------------");
	}

	private static void sizeTest(DB db, int numberOfUpdates, int numberOfInstruments) throws SQLException {
		db.createTable();

		List<Price> prices = new ArrayList<Price>();

		long startInsert = System.currentTimeMillis();

		System.out.print("\n" + db.getClass().getSimpleName() + " %");

		for (int upd = 0; upd < numberOfUpdates; upd++) {
			for (int instr = 0; instr < numberOfInstruments; instr++) {
				db.insert(Price.create(instr));
				// prices.add(Price.create(instr));
			}

			// if (upd % 10 == 0) {
			// db.insert(prices.toArray(new Price[1]));
			// prices.clear();
			// }
			if (upd % 100 == 0) {
				System.out.print(" " + (upd * 100) / numberOfUpdates);
			}
		}

		db.insert(prices.toArray(new Price[1]));

		System.out.println("\n" + db.getClass().getSimpleName() + ": Inserted " + numberOfInstruments + "x"
				+ numberOfUpdates + "=" + (numberOfInstruments * numberOfUpdates) + " entries in "
				+ ((double) (System.currentTimeMillis() - startInsert) / 1000) + "s");

	}
}
