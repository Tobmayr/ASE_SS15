package com.smartwg.core.internal.domain.dtos;

import java.math.BigDecimal;

import com.smartwg.core.internal.domain.entities.UserGroupCategory;

/**
 * 
 * @author ozde simsek (os) 6
 */
public class UserGroupCategoryDTO {

  private BigDecimal percent;
  private UserGroupDTO userGroupDTO;
  private CategoryDTO category;



  public UserGroupCategoryDTO(BigDecimal percent, CategoryDTO categoryDTO, UserGroupDTO userGroupDTO) {

    this.percent = percent;
    this.userGroupDTO = userGroupDTO;
    this.category = categoryDTO;

  }

  public UserGroupCategoryDTO(UserGroupCategory ugc) {
    this.percent = ugc.getPercent();
    this.userGroupDTO = new UserGroupDTO(ugc.getUserGroup());
    this.category = new CategoryDTO(ugc.getCategory());
  }



  public UserGroupCategoryDTO() {

  }

  public UserGroupDTO getUserGroupDTO() {
    return userGroupDTO;
  }

  public void setUserGroupDTO(UserGroupDTO userGroupDTO) {
    this.userGroupDTO = userGroupDTO;
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public BigDecimal getPercent() {
    return percent;
  }

  public void setPercent(BigDecimal percent) {
    this.percent = percent;
  }



}
