package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.PaymentFacade;
import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.services.PaymentService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PaymentFacadeImpl implements PaymentFacade {

  @Inject
  private PaymentService paymentService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#createOrUpdatePayment(com.smartwg.core.internal.domain.dtos.PaymentDTO)
   */

  @Override
  public Integer createOrUpdatePayment(PaymentDTO paymentDTO) {
    if (paymentDTO != null) {
      if (paymentDTO.getId() == null) {
        return paymentService.createPayment(paymentDTO);
      }
      return paymentService.editPayment(paymentDTO);
    }
    return -1;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#confirmUserPayment(com.smartwg.core.internal.domain.dtos.PaymentUserDTO)
   */

  @Override
  public void confirmUserPayment(PaymentUserDTO userPayment) {
    if (userPayment != null) {
      paymentService.confirmUserPayment(userPayment);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#findById(int)
   */

  @Override
  public PaymentDTO findById(int id) {
    return paymentService.findById(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#findUnconfirmedPaymentUsersByUserId(int)
   */

  @Override
  public List<PaymentUserDTO> findUnconfirmedPaymentUsersByUserId(int id) {
    return paymentService.findUnconfirmedPaymentUsersByUserId(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#findPaymentUsersByUserId(int)
   */

  @Override
  public List<PaymentUserDTO> findPaymentUsersByUserId(int id) {
    return paymentService.findPaymentUsersByUserId(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.PaymentFacade#createOrUpdatePaymentUsers(java.util.List,
   *      java.lang.Integer)
   */

  @Override
  public void createOrUpdatePaymentUsers(List<PaymentUserDTO> userPayments, Integer paymentId) {
    if (userPayments == null || userPayments.size() == 0) {
      paymentService.deletePaymentUsersByPaymentId(paymentId);
      return;
    }
    for (PaymentUserDTO puDTO : userPayments) {
      if (puDTO.getPaymentId() == null) {
        puDTO.setPaymentId(paymentId);
        paymentService.createPaymentUser(puDTO);
      } else {
        paymentService.editPaymentUser(puDTO);
      }
    }
  }
}
