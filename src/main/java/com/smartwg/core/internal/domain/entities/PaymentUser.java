package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.smartwg.core.internal.domain.entities.ids.PaymentUserId;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Entity
@Table(name = "payment_user")
@IdClass(PaymentUserId.class)
public class PaymentUser implements Serializable {

  private BigDecimal amount;
  private boolean confirmed;
  @Id
  @ManyToOne
  @JoinColumn(name = "payment_id", updatable = false, insertable = false,
      referencedColumnName = "id")
  private Payment payment;

  @Id
  @ManyToOne
  @JoinColumn(name = "sender_id", updatable = false, insertable = false,
      referencedColumnName = "id")
  private User sender;

  @Id
  @ManyToOne
  @JoinColumn(name = "receiver_id", updatable = false, insertable = false,
      referencedColumnName = "id")
  private User receiver;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public User getReceiver() {
    return receiver;
  }

  public void setReceiver(User receiver) {
    this.receiver = receiver;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PaymentUser))
      return false;

    PaymentUser that = (PaymentUser) o;
    return new EqualsBuilder().append(payment, that.payment).append(sender, that.sender)
        .append(receiver, that.receiver).append(amount, that.amount)
        .append(confirmed, that.confirmed).isEquals();

  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(payment).append(sender).append(receiver)
        .append(amount).append(confirmed).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("payment", payment).append("sender", sender)
        .append("receiver", receiver).append("amount", amount).append("confirmed", confirmed)
        .toString();
  }
}
