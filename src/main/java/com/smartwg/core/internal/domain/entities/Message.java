package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

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
@Table(name = "message")
public class Message implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "from_user_id")
  private User fromUser;

  @ManyToOne(optional = true)
  @JoinColumn(name = "to_user_id")
  private User toUser;

  @ManyToOne(optional = true)
  @JoinColumn(name = "group_id")
  private Group group;

  private String content;

  /**
   * default constructor for hibernate
   */
  public Message() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getFromUser() {
    return fromUser;
  }

  public void setFromUser(User fromUser) {
    this.fromUser = fromUser;
  }

  public User getToUser() {
    return toUser;
  }

  public void setToUser(User toUser) {
    this.toUser = toUser;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Message)) {
      return false;
    }

    Message message = (Message) o;

    return new EqualsBuilder().append(id, message.id).append(fromUser, message.fromUser)
        .append(toUser, message.toUser).append(group, message.group)
        .append(content, message.content).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(fromUser).append(toUser).append(group)
        .append(content).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("fromUser", fromUser)
        .append("toUser", toUser).append("group", group).append("content", content).toString();
  }
}
