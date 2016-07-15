package com.smartwg.core.internal.domain.entities.ids;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class PaymentUserId implements Serializable {

  private Integer payment;
  private Integer sender;
  private Integer receiver;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PaymentUserId))
      return false;
    PaymentUserId that = (PaymentUserId) o;
    return new EqualsBuilder().append(payment, that.payment).append(sender, that.sender)
        .append(receiver, that.receiver).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(payment).append(sender).append(receiver).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("payment", payment).append("sender", sender)
        .append("receiver", receiver).toString();
  }
}
