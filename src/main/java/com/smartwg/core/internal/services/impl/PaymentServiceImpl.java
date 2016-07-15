package com.smartwg.core.internal.services.impl;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.entities.Payment;
import com.smartwg.core.internal.domain.entities.PaymentUser;
import com.smartwg.core.internal.repositories.PaymentRepository;
import com.smartwg.core.internal.repositories.PaymentUserRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.PaymentService;

/**
 * @author Max Schwarzfischer (ms)
 */
@Named
public class PaymentServiceImpl implements PaymentService {

  @Inject
  private PaymentRepository paymentRepository;
  @Inject
  private PaymentUserRepository paymentUserRepository;
  @Inject
  private UserRepository userRepository;
  @Inject
  private EntityConverter entityFactory;


  @Override
  public Integer createPayment(PaymentDTO payment) {
    Payment paymentEntity = entityFactory.createPayment(payment);
    Payment result = paymentRepository.save(paymentEntity);
    if (result != null) {
      return result.getId();
    }
    return -1;
  }

  @Override
  public Integer editPayment(PaymentDTO payment) {
    Payment paymentEntity = entityFactory.createPayment(payment);
    if (paymentEntity != null) {
      paymentRepository.merge(paymentEntity);
      return paymentEntity.getId();
    }
    return -1;
  }

  @Override
  public void deletePaymentUsersByPaymentId(int id) {
    for (PaymentUserDTO dto : paymentRepository.findPaymentUsersByPaymentId(id)) {
      PaymentUser paymentUser = entityFactory.createPaymentUser(dto);
      if (paymentUser.getPayment() != null && paymentUser.getSender() != null
          && paymentUser.getReceiver() != null) {
        paymentUser.setPayment(paymentRepository.findById(paymentUser.getPayment().getId()));
        paymentUser.setSender(userRepository.findById(paymentUser.getSender().getId()));
        paymentUser.setReceiver(userRepository.findById(paymentUser.getReceiver().getId()));
        paymentUserRepository.delete(paymentUser);
      }
    }
  }

  @Override
  public void confirmUserPayment(PaymentUserDTO payment) {
    PaymentUser paymentUser = entityFactory.createPaymentUser(payment);
    if (paymentUser.getPayment() != null && paymentUser.getSender() != null
        && paymentUser.getReceiver() != null) {
      paymentUser.setPayment(paymentRepository.findById(paymentUser.getPayment().getId()));
      paymentUser.setSender(userRepository.findById(paymentUser.getSender().getId()));
      paymentUser.setReceiver(userRepository.findById(paymentUser.getReceiver().getId()));
      paymentUser.setConfirmed(true);
      paymentUserRepository.merge(paymentUser);
    }
  }

  @Override
  public PaymentDTO findById(int id) {
    Payment payment = paymentRepository.findById(id);
    if (payment != null) {
      return new PaymentDTO(payment);
    }
    return null;
  }

  @Override
  public List<PaymentUserDTO> findUnconfirmedPaymentUsersByUserId(int id) {
    return paymentRepository.findUnconfirmedPaymentUsersByUserId(id);
  }

  @Override
  public List<PaymentUserDTO> findPaymentUsersByUserId(int id) {
    return paymentRepository.findPaymentUsersByUserId(id);
  }

  @Override
  public void createPaymentUser(PaymentUserDTO puDTO) {
    PaymentUser paymentUser = entityFactory.createPaymentUser(puDTO);
    if (paymentUser.getPayment() != null && paymentUser.getSender() != null
        && paymentUser.getReceiver() != null) {
      paymentUser.setPayment(paymentRepository.findById(paymentUser.getPayment().getId()));
      paymentUser.setSender(userRepository.findById(paymentUser.getSender().getId()));
      paymentUser.setReceiver(userRepository.findById(paymentUser.getReceiver().getId()));
      paymentUserRepository.save(paymentUser);
    }
  }

  @Override
  public void editPaymentUser(PaymentUserDTO puDTO) {
    PaymentUser paymentUser = entityFactory.createPaymentUser(puDTO);
    if (paymentUser.getPayment() != null && paymentUser.getSender() != null
        && paymentUser.getReceiver() != null) {
      paymentUser.setPayment(paymentRepository.findById(paymentUser.getPayment().getId()));
      paymentUser.setSender(userRepository.findById(paymentUser.getSender().getId()));
      paymentUser.setReceiver(userRepository.findById(paymentUser.getReceiver().getId()));
      paymentUser.setAmount(puDTO.getAmount());
      paymentUserRepository.merge(paymentUser);
    }
  }
}
