package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.entities.Payment;

import java.util.List;

/**
 * @author Max Schwarzfischer (ms)
 */
public interface PaymentRepository extends GenericRepository<Payment> {

  /**
   * Finds all PaymentUsers with the given payment id
   *
   * @param id payment id
   * @return a List of all PaymentUsers with the given payment id. If there are no according
   *         PaymentUsers in the database, the list will be empty
   */
  List<PaymentUserDTO> findPaymentUsersByPaymentId(Integer id);

  /**
   * Finds all unconfirmed PaymentUsers where a given user is either receiver or sender
   *
   * @param id user id
   * @return a List of all payments where the given user is either receiver or sender or an empty
   *         list, if no such entries exist in the database
   */
  List<PaymentUserDTO> findUnconfirmedPaymentUsersByUserId(Integer id);

  /**
   * Finds all PaymentUsers where a given user is either receiver or sender
   *
   * @param id user id
   * @return a List of all payments where the given user is either receiver or sender or an empty
   *         list, if no such entries exist in the database
   */
  List<PaymentUserDTO> findPaymentUsersByUserId(Integer id);
}
