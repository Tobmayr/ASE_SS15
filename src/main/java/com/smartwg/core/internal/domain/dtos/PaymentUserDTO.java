package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.PaymentUser;

import java.math.BigDecimal;

/**
 * @author Max Schwarzfischer (ms)
 */
public class PaymentUserDTO {

  private BigDecimal amount;
  private boolean confirmed;
  private UserDTO sender;
  private UserDTO receiver;
  private Integer paymentId;

  public PaymentUserDTO() {}

  public PaymentUserDTO(final BigDecimal amount, final boolean confirmed, final UserDTO sender,
      final UserDTO receiver, final Integer paymentId) {
    this.amount = amount;
    this.confirmed = confirmed;
    this.sender = sender;
    this.receiver = receiver;
    this.paymentId = paymentId;
  }

  public PaymentUserDTO(final PaymentUser paymentUser) {
    this.amount = paymentUser.getAmount();
    this.confirmed = paymentUser.isConfirmed();
    this.sender = new UserDTO(paymentUser.getSender());
    this.receiver = new UserDTO(paymentUser.getReceiver());
    this.paymentId = paymentUser.getPayment().getId();
  }

  public boolean isConfirmed() {
    return this.confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  public UserDTO getSender() {
    return this.sender;
  }

  public void setSender(UserDTO sender) {
    this.sender = sender;
  }

  public UserDTO getReceiver() {
    return this.receiver;
  }

  public void setReceiver(UserDTO receiver) {
    this.receiver = receiver;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getPaymentId() {
    return this.paymentId;
  }

  public void setPaymentId(Integer paymentId) {
    this.paymentId = paymentId;
  }
}
