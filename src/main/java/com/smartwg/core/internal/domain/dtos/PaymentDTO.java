package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.Payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * @author Max Schwarzfischer (ms)
 */
public class PaymentDTO {
  private Integer id;
  private Date date;
  private byte[] paymentLog;
  private List<PaymentUserDTO> userPayments = new ArrayList<>();

  public PaymentDTO() {}

  public PaymentDTO(final Integer id, final Date date, final byte[] paymentLog) {
    this.id = id;
    this.date = date;
    this.paymentLog = paymentLog;
  }

  public PaymentDTO(final Payment payment) {
    if (payment != null) {
      this.id = payment.getId();
      this.date = payment.getDate();
      this.paymentLog = payment.getPaymentLog();
    }
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.date = calendar.getTime();
  }

  public byte[] getPaymentLog() {
    return this.paymentLog;
  }

  public void setPaymentLog(byte[] paymentLog) {
    this.paymentLog = paymentLog;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<PaymentUserDTO> getUserPayments() {
    return userPayments;
  }

  public void setUserPayments(List<PaymentUserDTO> userPayments) {
    this.userPayments = userPayments;
  }
}
