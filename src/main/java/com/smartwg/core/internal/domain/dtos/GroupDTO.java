package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.Country;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.UserGroup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * @author Anna Sadriu (as)
 */
public class GroupDTO {

  private Integer id;
  private String name;
  private CountryDTO country;
  private String city;
  private String zip;
  private String street;
  private String street2;
  private List<UserGroupDTO> userGroups = new ArrayList<>();


  public GroupDTO() {}

  public GroupDTO(Integer id, String name, Country country, String city, String zip, String street,
      String street2, List<UserGroupDTO> userGroups) {
    this.id = id;
    this.name = name;

    this.country = new CountryDTO(country);

    this.city = city;
    this.zip = zip;
    this.street = street;
    this.street2 = street2;
  }

  public GroupDTO(Integer id, String name, Country country, String city, String zip, String street,
      String street2) {
    this.id = id;
    this.name = name;
    this.country = new CountryDTO(country);
    this.city = city;
    this.zip = zip;
    this.street = street;
    this.street2 = street2;
  }

  public GroupDTO(Group group) {
    this.id = group.getId();
    this.name = group.getName();
    this.country = new CountryDTO(group.getCountry());
    this.city = group.getCity();
    this.zip = group.getZip();
    this.street = group.getStreet();
    this.street2 = group.getStreet2();
    List<UserGroup> userGroups = group.getUserGroup();
    for (UserGroup ug : userGroups) {
      this.userGroups.add(new UserGroupDTO(ug));
    }
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof GroupDTO))
      return false;
    GroupDTO that = (GroupDTO) obj;
    return new EqualsBuilder().append(this.id, that.id).append(this.name, that.name)
        .append(this.country, that.country).append(this.city, that.city).append(this.zip, that.zip)
        .append(this.street, that.street).append(this.street2, that.street2).isEquals();
  }

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


  public CountryDTO getCountry() {
    return country;
  }

  public void setCountry(CountryDTO country) {

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

  public List<UserGroupDTO> getUserGroups() {
    return userGroups;
  }

  public void setUserGroups(List<UserGroupDTO> userGroups) {
    this.userGroups = userGroups;
  }

}
