package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.repositories.PaymentRepository;
import com.smartwg.core.util.Constants;

public class PaymentRepositoryImplTest {

  @InjectMocks
  private PaymentRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private PaymentUserDTO paymentUserDTO;

  @BeforeMethod
  public void setUp() {
    repository = new PaymentRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findPaymentUsersByPaymentId_Succeed() {
    when(entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_PAYMENT_ID)).thenReturn(
        query);
    when(query.setParameter("id", 1)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(paymentUserDTO));

    final List<PaymentUserDTO> result = repository.findPaymentUsersByPaymentId(1);

    Assert.assertEquals(result, Collections.singletonList(paymentUserDTO));
  }

  @Test
  public void findPaymentUsersByPaymentId_Fail() {
    when(entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_PAYMENT_ID)).thenReturn(
        query);
    when(query.setParameter("id", -1)).thenReturn(query);
    when(query.getResultList()).thenReturn(new ArrayList<>());

    final List<PaymentUserDTO> result = repository.findPaymentUsersByPaymentId(-1);

    Assert.assertNotNull(result);
    Assert.assertEquals(result.size(), 0);
  }

  @Test
  public void findUnconfirmedPaymentUsersByUserId_Succeed() {
    when(entityManager.createNamedQuery(Constants.QUERY_UNCONFIRMED_PAYMENT_USERS_BY_USER_ID))
        .thenReturn(query);
    when(query.setParameter("id", 1)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(paymentUserDTO));

    final List<PaymentUserDTO> result = repository.findUnconfirmedPaymentUsersByUserId(1);

    Assert.assertEquals(result, Collections.singletonList(paymentUserDTO));
  }

  @Test
  public void findUnconfirmedPaymentUsersByUserId_Fail() {
    when(entityManager.createNamedQuery(Constants.QUERY_UNCONFIRMED_PAYMENT_USERS_BY_USER_ID))
        .thenReturn(query);
    when(query.setParameter("id", -1)).thenReturn(query);
    when(query.getResultList()).thenReturn(new ArrayList<>());

    final List<PaymentUserDTO> result = repository.findUnconfirmedPaymentUsersByUserId(-1);

    Assert.assertNotNull(result);
    Assert.assertEquals(result.size(), 0);
  }

  @Test
  public void findPaymentUsersByUserId_Succeed() {
    when(entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_USER_ID))
        .thenReturn(query);
    when(query.setParameter("id", 1)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(paymentUserDTO));

    final List<PaymentUserDTO> result = repository.findPaymentUsersByUserId(1);

    Assert.assertEquals(result, Collections.singletonList(paymentUserDTO));
  }

  @Test
  public void findPaymentUsersByUserId_Fail() {
    when(entityManager.createNamedQuery(Constants.QUERY_PAYMENT_USERS_BY_USER_ID))
        .thenReturn(query);
    when(query.setParameter("id", -1)).thenReturn(query);
    when(query.getResultList()).thenReturn(new ArrayList<>());

    final List<PaymentUserDTO> result = repository.findPaymentUsersByUserId(-1);

    Assert.assertNotNull(result);
    Assert.assertEquals(result.size(), 0);
  }
}
