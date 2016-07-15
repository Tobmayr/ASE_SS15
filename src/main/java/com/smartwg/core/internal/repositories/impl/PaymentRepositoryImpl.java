package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.entities.Payment;
import com.smartwg.core.internal.repositories.PaymentRepository;
import com.smartwg.core.util.Constants;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Max Schwarzfischer (ms)
 */
@Named
public class PaymentRepositoryImpl extends GenericRepositoryImpl<Payment> implements
    PaymentRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentUserDTO> findPaymentUsersByPaymentId(Integer id) {
    Query query = entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_PAYMENT_ID);
    return query.setParameter("id", id).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentUserDTO> findUnconfirmedPaymentUsersByUserId(Integer id) {
    Query query =
        entityManager.createNamedQuery(Constants.QUERY_UNCONFIRMED_PAYMENT_USERS_BY_USER_ID);
    return query.setParameter("id", id).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentUserDTO> findPaymentUsersByUserId(Integer id) {
    Query query = entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_USER_ID);
    return query.setParameter("id", id).getResultList();
  }

}
