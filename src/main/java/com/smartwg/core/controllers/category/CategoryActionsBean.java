package com.smartwg.core.controllers.category;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.util.Constants;

/**
 * 
 * @author ozdesimsek
 *
 */
@Named("categoryActions")
@Scope("session")
public class CategoryActionsBean {

  @Inject
  private NavigationBean navigation;

  @Inject
  private CategoryFacade facade;

  @PostConstruct
  public void init() {

  }

  public String newCategory(String returnPage) {
    return navigation.getPageNewCategory() + Constants.PAGE_REDIRECT + "&returnPage=" + returnPage;
  }

  public String showCategory(final CategoryDTO categoryDTO, String returnPage) {
    return navigation.getPageShowCategory() + Constants.PAGE_REDIRECT + "&categoryId="
        + categoryDTO.getId() + "&returnPage=" + returnPage;
  }

  public String editCategory(final CategoryDTO categoryDTO, String returnPage) {

    return navigation.getPageNewCategory() + Constants.PAGE_REDIRECT + "&categoryId="
        + categoryDTO.getId() + "&returnPage=" + returnPage;
  }

  public String deleteCategory(final CategoryDTO categoryDTO, String returnPage) {
    facade.deleteCategory(categoryDTO);
    return returnPage + Constants.PAGE_REDIRECT + "&tab=1";
  }


}
