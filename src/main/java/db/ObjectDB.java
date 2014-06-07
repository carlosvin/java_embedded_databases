package db;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import domain.Price;

public class ObjectDB implements DB {

	private static final String TABLE_NAME = Price.class.getSimpleName();
	private final EntityManager em;
	private final EntityManagerFactory emf;

	public ObjectDB(String folderPath) {
		emf = Persistence.createEntityManagerFactory(folderPath + "/prices.odb");
		em = emf.createEntityManager();
		// em.setFlushMode(FlushModeType.COMMIT);
	}

	@Override
	public void insert(Price... prices) throws SQLException {
		em.getTransaction().begin();
		for (Price p : prices) {
			em.persist(p);
		}
		em.getTransaction().commit();
	//	em.clear();

	}

	@Override
	public void createTable() {
		// it is not needed
	}

	@Override
	public void deleteAll() {
		em.getTransaction().begin();
		for (Price p : selectAll()) {
			em.remove(p);
		}
		em.getTransaction().commit();
	}

	@Override
	public Set<Price> selectAll() {
		TypedQuery<Price> query = em.createQuery("SELECT p FROM " + TABLE_NAME + " p", Price.class);
		return new HashSet<>(query.getResultList());
	}

	@Override
	public void update(Price... prices) {
		em.getTransaction().begin();
		for (Price p : selectAll()) {
			em.refresh(p);
		}
		em.getTransaction().commit();
	}
}
