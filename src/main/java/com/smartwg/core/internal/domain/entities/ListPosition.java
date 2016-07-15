package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "list_position")
public class ListPosition implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id")
  private Category category;

  private int amount;

  @Column(name = "list_order")
  private int order;

  @Column(nullable = false, name = "is_done")
  private boolean done;

  @ManyToOne(optional = false)
  @JoinColumn(name = "list_id", nullable = false)
  private ShoppingList shoppingList;

  /**
   * default constructor for hibernate
   */
  public ListPosition() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public void setShoppingList(ShoppingList shoppingList) {
    this.shoppingList = shoppingList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ListPosition)) {
      return false;
    }

    ListPosition position = (ListPosition) o;

    return new EqualsBuilder().append(amount, position.amount).append(order, position.order)
        .append(done, position.done).append(id, position.id).append(name, position.name)
        .append(category, position.category).append(shoppingList, position.shoppingList).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(category).append(amount)
        .append(order).append(done).append(shoppingList).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("category", category).append("amount", amount).append("order", order)
        .append("done", done).toString();
  }
}
