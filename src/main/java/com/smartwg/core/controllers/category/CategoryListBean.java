package com.smartwg.core.controllers.category;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;

@Named("categoryList")
@Scope("view")
public class CategoryListBean {

  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private UserBean userBean;
  private List<CategoryDTO> categories;

  @PostConstruct
  public void init() {
    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }



}
