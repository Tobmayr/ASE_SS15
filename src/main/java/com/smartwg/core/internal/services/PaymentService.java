package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;

import java.util.List;

/**
 * @author Max Schwarzfischer (ms)
 */
public interface PaymentService {

  /**
   * Confirms a paymentUser in the database (=sets confirmed flag to true)
   *
   * @param payment paymentUser object which should be confirmed
   */
  void confirmUserPayment(PaymentUserDTO payment);

  /**
   * Finds a payment object by its id
   *
   * @param id id of the payment, which should be searched and returned
   * @return PaymentDTO or null, if a Payment with the passed id was not found in the database
   */
  PaymentDTO findById(int id);


  /**
   * Delets all PaymentUsers with the given payment id from the database
   *
   * @param id payment id
   */
  void deletePaymentUsersByPaymentId(int id);

  /**
   * Finds all unconfirmed PaymentUsers where a given user is either receiver or sender
   *
   * @param id user id
   * @return a List of all PaymentUsers where the given user is either receiver or sender or an
   *         empty list, if no such entries exist in the database
   */
  List<PaymentUserDTO> findUnconfirmedPaymentUsersByUserId(int id);

  /**
   * Finds all PaymentUsers where a given user is either receiver or sender
   *
   * @param id user id
   * @return a List of all PaymentUsers where the given user is either receiver or sender or an
   *         empty list, if no such entries exist in the database
   */
  List<PaymentUserDTO> findPaymentUsersByUserId(int id);

  /**
   * Persists a new payment oin the database
   *
   * @param payment payment object which should be stored
   * @return payment Id, if the creation succeeded, otherwise -1
   */
  Integer createPayment(PaymentDTO payment);

  /**
   * Updates an existing payment object in the database
   *
   * @param payment payment object which should be updated
   * @return payment Id if the update succeeded, otherwise -1
   */
  Integer editPayment(PaymentDTO payment);

  /**
   * Persists a new paymentUser in the database
   *
   * @param puDTO paymentUser object which should be stored
   */
  void createPaymentUser(PaymentUserDTO puDTO);

  /**
   * Updates an existing paymentUser object in the database
   *
   * @param puDTO paymentUser object which should be updated
   */
  void editPaymentUser(PaymentUserDTO puDTO);
}
