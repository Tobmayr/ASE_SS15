package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks), Tobias Ortmayr (to)
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username", unique = true)
  private String userName;

  @Column(length = 20)
  private byte[] password;

  @Column(length = 8)
  private byte[] salt;

  @Column(unique = true, name = "e_mail")
  private String email;

  @Column(name = "birth_date")
  private Date birthDate;

  private BigDecimal income;

  @Column(nullable = false)
  private String notify;

  private boolean confirmed = false;

  @Column(name = "confirm_code")
  private String confirmCode;

  @Column(name = "deleted")
  private boolean deleted;

  @Column(name = "locale")
  private String locale;
  @OneToMany(mappedBy = "fromUser")
  private List<Message> sendMessages = new ArrayList<Message>();

  @OneToMany(mappedBy = "toUser")
  private List<Message> receivedMessages = new ArrayList<Message>();

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "user")
  private List<UserGroup> groups = new ArrayList<UserGroup>();

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "user")
  private List<UserActivity> ratings = new ArrayList<UserActivity>();

  @OneToMany(mappedBy = "createdBy")
  private List<Activity> createdActivities = new ArrayList<Activity>();

  @OneToMany(mappedBy = "assignedTo")
  private List<Activity> assignedActivities = new ArrayList<Activity>();

  @OneToMany(mappedBy = "createdBy")
  private List<ShoppingList> createdShoppingLists = new ArrayList<ShoppingList>();

  @OneToMany(mappedBy = "assignedTo")
  private List<ShoppingList> assignedShoppingLists = new ArrayList<ShoppingList>();

  @OneToMany(mappedBy = "sender")
  private List<PaymentUser> paymentSenders = new ArrayList<PaymentUser>();

  @OneToMany(mappedBy = "receiver")
  private List<PaymentUser> paymentReceivers = new ArrayList<PaymentUser>();


  /**
   * default constructor for hibernate
   */
  public User() {}



  public String getLocale() {
    return locale;
  }



  public void setLocale(String locale) {
    this.locale = locale;
  }



  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public byte[] getPassword() {
    return password;
  }

  public void setPassword(byte[] password) {
    this.password = password;
  }

  public byte[] getSalt() {
    return salt;
  }

  public void setSalt(byte[] salt) {
    this.salt = salt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public BigDecimal getIncome() {
    return income;
  }

  public void setIncome(BigDecimal income) {
    this.income = income;
  }

  public String getNotify() {
    return notify;
  }

  public void setNotify(String notify) {
    this.notify = notify;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  public String getConfirmCode() {
    return confirmCode;
  }

  public void setConfirmCode(String confirmCode) {
    this.confirmCode = confirmCode;
  }

  public List<Message> getSendMessages() {
    return sendMessages;
  }

  public void setSendMessages(List<Message> sendMessages) {
    this.sendMessages = sendMessages;
  }

  public List<Message> getReceivedMessages() {
    return receivedMessages;
  }

  public void setReceivedMessages(List<Message> receivedMessages) {
    this.receivedMessages = receivedMessages;
  }

  public List<UserGroup> getGroups() {
    return groups;
  }

  public void setGroups(List<UserGroup> groups) {
    this.groups = groups;
  }

  public List<UserActivity> getRatings() {
    return ratings;
  }

  public void setRatings(List<UserActivity> ratings) {
    this.ratings = ratings;
  }

  public List<Activity> getCreatedActivities() {
    return createdActivities;
  }

  public void setCreatedActivities(List<Activity> createdActivities) {
    this.createdActivities = createdActivities;
  }

  public List<Activity> getAssignedActivities() {
    return assignedActivities;
  }

  public void setAssignedActivities(List<Activity> assignedActivities) {
    this.assignedActivities = assignedActivities;
  }

  public List<ShoppingList> getCreatedShoppingLists() {
    return createdShoppingLists;
  }

  public void setCreatedShoppingLists(List<ShoppingList> createdShoppingLists) {
    this.createdShoppingLists = createdShoppingLists;
  }

  public List<ShoppingList> getAssignedShoppingLists() {
    return assignedShoppingLists;
  }

  public void setAssignedShoppingLists(List<ShoppingList> assignedShoppingLists) {
    this.assignedShoppingLists = assignedShoppingLists;
  }


  public List<PaymentUser> getPaymentSenders() {
    return paymentSenders;
  }



  public boolean isDeleted() {
    return deleted;
  }



  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }



  public void setPaymentSenders(List<PaymentUser> paymentSenders) {
    this.paymentSenders = paymentSenders;
  }



  public List<PaymentUser> getPaymentReceivers() {
    return paymentReceivers;
  }



  public void setPaymentReceivers(List<PaymentUser> paymentReceivers) {
    this.paymentReceivers = paymentReceivers;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof User)) {
      return false;
    }

    User user = (User) o;

    return new EqualsBuilder().append(notify, user.notify).append(confirmed, user.confirmed)
        .append(id, user.id).append(firstName, user.firstName).append(lastName, user.lastName)
        .append(userName, user.userName).append(password, user.password).append(salt, user.salt)
        .append(email, user.email).append(birthDate, user.birthDate).append(income, user.income)
        .append(confirmCode, user.confirmCode).append(sendMessages, user.sendMessages)
        .append(receivedMessages, user.receivedMessages).append(groups, user.groups)
        .append(ratings, user.ratings).append(createdActivities, user.createdActivities)
        .append(assignedActivities, user.assignedActivities)
        .append(createdShoppingLists, user.createdShoppingLists)
        .append(assignedShoppingLists, user.assignedShoppingLists)
        .append(this.locale, user.lastName).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(firstName).append(lastName)
        .append(userName).append(password).append(salt).append(email).append(birthDate)
        .append(income).append(notify).append(confirmed).append(confirmCode).append(sendMessages)
        .append(receivedMessages).append(groups).append(ratings).append(createdActivities)
        .append(assignedActivities).append(createdShoppingLists).append(assignedShoppingLists)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("confirmCode", confirmCode).append("id", id)
        .append("firstName", firstName).append("lastName", lastName).append("userName", userName)
        .append("password", password).append("salt", salt).append("email", email)
        .append("birthDate", birthDate).append("income", income).append("notify", notify)
        .append("confirmed", confirmed).toString();
  }



  public void addUserGroup(UserGroup userGroup) {
    groups.add(userGroup);

  }
}
