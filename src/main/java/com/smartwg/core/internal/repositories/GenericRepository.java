package com.smartwg.core.internal.repositories;

import java.util.List;

/**
 * Generic repository supporting basic hibernate operations
 *
 * @author Kamil Sierzant (ks)
 */
public interface GenericRepository<T> {

  T findById(Integer id);

  List<T> findAll();

  T save(T entity);

  void merge(T entity);

  void delete(T entity);

  void flush();
}
