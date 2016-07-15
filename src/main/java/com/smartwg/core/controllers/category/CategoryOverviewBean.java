package com.smartwg.core.controllers.category;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;

@Named("categoryOverview")
@Scope("view")
public class CategoryOverviewBean {
  private static final Logger logger = LoggerFactory.getLogger(CategoryOverviewBean.class);
  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private UserGroupCategoryFacade ugcFacade;

  private CategoryDTO category;
  private List<UserGroupCategoryDTO> ugcList;
  private String returnPage;

  @PostConstruct
  public void setUp() {
    final Map<String, String> requestParameterMap =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String categoryId = requestParameterMap.get("categoryId");
    returnPage = requestParameterMap.get("returnPage") + "?tab=1";

    if (categoryId != null) {
      Integer id = Integer.valueOf(categoryId);
      logger.info("CatID: " + id);
      category = categoryFacade.findById(id);

      ugcList = ugcFacade.findByCategory(id);
      logger.info("Uglist size;" + ugcList.size());
    } else {
      category = new CategoryDTO();
    }

  }



  public CategoryDTO getCategory() {
    return category;
  }



  public void setCategory(CategoryDTO category) {
    this.category = category;
  }



  public List<UserGroupCategoryDTO> getUgcList() {
    return ugcList;
  }



  public void setUgcList(List<UserGroupCategoryDTO> ugcList) {
    this.ugcList = ugcList;
  }



  public String getReturnPage() {
    return returnPage;
  }

  public void setReturnPage(String returnPage) {
    this.returnPage = returnPage;
  }



}
