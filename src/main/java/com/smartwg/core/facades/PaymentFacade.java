package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
/**
 * Central access point for service logic which is associated with payments.
 * e.g. persisting and updating payments in the database, confirming paymentsetc.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author Max Schwarzfischer (ms)
 */
public interface PaymentFacade {

  /**
   * Persists a new payment or updates an existing payment in the database
   *
   * @param paymentDTO payment object which should be stored
   * @return payment Id if the creation or update succeeded, otherwise -1
   */
  Integer createOrUpdatePayment(PaymentDTO paymentDTO);

  /**
   * Confirms a paymentUser in the database (=sets confirmed flag to true)
   *
   * @param paymentUser paymentUser object which should be confirmed
   */
  void confirmUserPayment(PaymentUserDTO paymentUser);

  /**
   * Finds a payment object by its id
   *
   * @param id id of the payment, which should be searched and returned
   * @return PaymentDTO or null, if a Payment with the passed id was not found in the database
   */
  PaymentDTO findById(int id);

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
   * Creates or updates all PaymentUsers in the list.
   *
   * @param paymentUsers a list of all PaymentUsers, that shall be created or updated. If the list
   *        is empty or null, all PaymentUsers with the given paymentId shall be deleted
   * @param paymentId the id of the payment, for which paymentUsers shall be created or updated
   */
  void createOrUpdatePaymentUsers(List<PaymentUserDTO> paymentUsers, Integer paymentId);

}
