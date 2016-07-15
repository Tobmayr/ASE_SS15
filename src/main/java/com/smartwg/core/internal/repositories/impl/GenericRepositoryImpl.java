package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.repositories.GenericRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @param <T> of the target entity
 */
@Named
public abstract class GenericRepositoryImpl<T extends Serializable> implements GenericRepository<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericRepositoryImpl.class);

  @PersistenceContext
  protected EntityManager em;

  protected Class<T> entityClass;

  @SuppressWarnings("unchecked")
  public GenericRepositoryImpl() {
    final ParameterizedType genericSuperclass =
        (ParameterizedType) getClass().getGenericSuperclass();
    this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
  }

  @Override
  public T findById(final Integer id) {
    LOGGER.info(String.format("Trying to find an entity of type '%s' with id %d",
        entityClass.getSimpleName(), id));
    return em.find(entityClass, id);
  }

  @Override
  public List<T> findAll() {
    final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
    final Root<T> rootQuery = criteriaQuery.from(entityClass);
    criteriaQuery.select(rootQuery);
    final TypedQuery<T> query = em.createQuery(criteriaQuery);

    return query.getResultList();
  }

  @Override
  public T save(final T entity) {
    LOGGER.info(String.format("Saving entity of type '%s'", entityClass.getSimpleName()));

    em.persist(entity);
    flush();
    return entity;
  }

  @Override
  public void merge(final T entity) {
    LOGGER.info(String.format("Merging entity of type '%s'", entityClass.getSimpleName()));

    em.merge(entity);
  }

  @Override
  public void delete(final T entity) {
    LOGGER.info(String.format("Deleting entity of type '%s'", entityClass.getSimpleName()));

    em.remove(em.contains(entity) ? entity : em.merge(entity));
  }

  @Override
  public void flush() {
    em.flush();
  }
}
