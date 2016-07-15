package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Tobias Ortmayr (to)
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Date date;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "log", length = 16777215)
  private byte[] paymentLog;

  @OneToMany(mappedBy = "payment")
  private List<PaymentUser> paymentUsers = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public byte[] getPaymentLog() {
    return paymentLog;
  }

  public void setPaymentLog(byte[] paymentLog) {
    this.paymentLog = paymentLog;
  }

  public void addPaymentUser(PaymentUser paymentUser) {
    paymentUsers.add(paymentUser);
  }

  public List<PaymentUser> getPaymentUsers() {
    return paymentUsers;
  }

  public void setPaymentUsers(List<PaymentUser> paymentUsers) {
    this.paymentUsers = paymentUsers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Payment)) {
      return false;
    }

    Payment payment = (Payment) o;

    return new EqualsBuilder().append(id, payment.id).append(date, payment.date)
        .append(paymentLog, payment.paymentLog).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(date).append(paymentLog).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("date", date).append(paymentLog)
        .toString();
  }
}
