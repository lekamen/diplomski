package hr.lenak.diplomski.core.repository;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;


@Transactional
@SuppressWarnings("unchecked")
public class QueryDslRepository<E, ID> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@PersistenceContext()
	protected EntityManager em;

	private Class<E> entityClass;

	public QueryDslRepository() {
		ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<E>) pt.getActualTypeArguments()[0];
	}

	public E findById(ID id) {
		return em.find(entityClass, id);
	}
	
	public E update(E entity) {
		log.debug("Update entiteta: {}", entity);
		return em.merge(entity);
	}
	
	public E create(E entity) {
		log.debug("Kreiranje entiteta: {}", entity);
		em.persist(entity);
		return entity;
	}

	public void delete(E entity) {
		log.debug("Delete entiteta: {}", entity);
		Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
		em.remove(em.getReference(entityClass, id));
	}

	protected <X> JPAQuery<X> select(Expression<X> expression) {
		return new JPAQuery<>(em).select(expression);
	}

	public JPAQuery<Tuple> select(Expression<?>... expressions) {
		return new JPAQuery<>(em).select(expressions);
	}
}
