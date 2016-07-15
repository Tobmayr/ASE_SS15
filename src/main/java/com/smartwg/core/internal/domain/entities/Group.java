package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks), Anna Sadriu (as)
 */
@Entity
@Table(name = "\"group\"")
public class Group implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @ManyToOne()
  @JoinColumn(name = "country")
  private Country country;

  @Column(length = 45)
  private String city;

  @Column(length = 10)
  private String zip;

  @Column(length = 255)
  private String street;

  @Column(length = 255)
  private String street2;


  @OneToMany(mappedBy = "group", orphanRemoval = true)
  private List<Activity> activities = new ArrayList<>();

  @OneToMany(mappedBy = "group", orphanRemoval = true)
  private List<ShoppingList> lists = new ArrayList<>();

  @OneToMany(mappedBy = "group")
  private List<Message> messages = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "group_enabled_feature", joinColumns = @JoinColumn(name = "feature_id"),
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Feature> features = new ArrayList<>();

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST, CascadeType.DETACH},
      orphanRemoval = true)
  private List<UserGroup> roles = new ArrayList<>();

  @OneToMany(mappedBy = "group", orphanRemoval = true)
  private List<Bill> bills = new ArrayList<>();

  @OneToMany(mappedBy = "group", cascade = CascadeType.DETACH, orphanRemoval = true)
  private List<UserGroup> userGroup = new ArrayList<>();

  @OneToMany(mappedBy = "group", orphanRemoval = true)
  private List<Category> categories = new ArrayList<>();

  @OneToMany(mappedBy = "group", orphanRemoval = true)
  private List<Shop> shops = new ArrayList<Shop>();

  /**
   * default constructor for hibernate
   */
  public Group() {}

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

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public List<Activity> getActivities() {
    return activities;
  }

  public void setActivities(List<Activity> activities) {
    this.activities = activities;
  }

  public void addActivity(Activity activity) {
    this.activities.add(activity);
  }

  public List<ShoppingList> getLists() {
    return lists;
  }

  public void setLists(List<ShoppingList> lists) {
    this.lists = lists;
  }

  public void addList(ShoppingList list) {
    this.lists.add(list);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public void addFeature(Feature feature) {
    this.features.add(feature);
  }

  public List<UserGroup> getRoles() {
    return roles;
  }

  public void setRoles(List<UserGroup> roles) {
    this.roles = roles;
  }

  public List<Bill> getBills() {
    return bills;
  }

  public void setBills(List<Bill> bills) {
    this.bills = bills;
  }

  public List<UserGroup> getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(List<UserGroup> userGroup) {
    this.userGroup = userGroup;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public void addCategory(Category category) {
    this.categories.add(category);
  }

  public List<Shop> getShops() {
    return shops;
  }

  public void setShops(List<Shop> shops) {
    this.shops = shops;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Group)) {
      return false;
    }

    Group group = (Group) o;

    return new EqualsBuilder().append(id, group.id).append(name, group.name)
        .append(country, group.country).append(city, group.city).append(zip, group.zip)
        .append(street, group.street).append(street2, group.street2)
        .append(categories, group.categories).append(activities, group.activities)
        .append(lists, group.lists).append(messages, group.messages)
        .append(features, group.features).append(roles, group.roles).append(bills, group.bills)
        .append(userGroup, group.userGroup).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(country).append(city)
        .append(zip).append(street).append(street2).append(categories).append(activities)
        .append(lists).append(messages).append(features).append(roles).append(bills)
        .append(userGroup).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("country", country).append("city", city).append("zip", zip)
        .append("street", street).append("street2", street2).toString();
  }
}
