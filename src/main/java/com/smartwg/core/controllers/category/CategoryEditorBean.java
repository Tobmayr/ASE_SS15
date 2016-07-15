package com.smartwg.core.controllers.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;
import com.sun.jna.platform.win32.WinDef.UCHAR;

@Named("CategoryEditor")
@Scope("view")
public class CategoryEditorBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryEditorBean.class);
  
  private ResourceBundle ms = SmartWG.getMessageBundle();

  @Inject
  private CategoryFacade categoryFacade;

  @Inject
  private NavigationBean navigation;
  @Inject
  private UserBean userBean;
  @Inject
  private UserGroupCategoryFacade userGroupCategoryFacade;

  private GroupDTO currentGroup;
  private CategoryDTO category;

  private List<UserGroupCategoryDTO> ugcList;
  private boolean isDefault;
  private List<CategoryDTO> categories;
  private String returnPage;

  public CategoryEditorBean() {}

  @PostConstruct
  private void init() {
    LOGGER.info("categorizationBean init-methode called");
    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    ugcList = new ArrayList<UserGroupCategoryDTO>();
    String categoryId = requestParameterMap.get("categoryId");
    returnPage = requestParameterMap.get("returnPage") + "?tab=1";;
    if (categoryId != null) {
      final Integer id = Integer.valueOf(categoryId);
      LOGGER.info("ID " + id);
      category = categoryFacade.findById(id);
      ugcList = userGroupCategoryFacade.findByCategory(id);
      if (ugcList.isEmpty()) {
        generateUGCList();
      }


    } else {
      category = new CategoryDTO();
      generateUGCList();
    }

    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());


  }

  private void generateUGCList() {

    for (UserDTO u : userBean.getUsersOfCurrentGroup()) {
      UserGroupCategoryDTO ugc = new UserGroupCategoryDTO();
      UserGroupDTO ug = new UserGroupDTO();
      ug.setGroupId(userBean.getCurrentUserGroup().getGroupId());
      ug.setUserId(u.getId());
      ug.setUserName(u.getUserName());
      ugc.setUserGroupDTO(ug);
      ugc.setPercent(new BigDecimal(0.0));
      ugc.setCategory(category);
      ugcList.add(ugc);
    }

  }

  public GroupDTO getCurrentGroup() {
    return currentGroup;
  }

  public void setCurrentGroup(GroupDTO currentGroup) {
    this.currentGroup = currentGroup;
  }

  public List<UserGroupCategoryDTO> getUgcList() {
    return ugcList;
  }

  public void setUgcList(List<UserGroupCategoryDTO> ugcList) {
    this.ugcList = ugcList;
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public void reset() {
    category = new CategoryDTO();
    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());
  }

  public String createCategory(boolean docontinue) {
    LOGGER.info("createCategory() invoked");
    Integer groupId = userBean.getCurrentUserGroup().getGroupId();
    if (category.getGroup_id() == null) {
      category.setGroup_id(groupId);

    }
    if (category.getI18NKey() != null) {
      if (!category.getName().equals(ms.getString(category.getI18NKey()))) {
        category.setDefaultCategory(false);
      } else {
          category.setName(category.getI18NKey());
      }
    }
    Integer id = categoryFacade.saveCategory(category);
    category.setId(id);
    if (category.isCustomDivision()) {
      for (UserGroupCategoryDTO ugc : ugcList) {
        userGroupCategoryFacade.createUserGroupCategory(ugc);
      }

    }
    if (docontinue)
      return navigation.getPageNewCategory();
    return navigation.getPageGroupAdministration() + "?tab=1";
  }

  public String onFlowProcess(FlowEvent event) {

    if (event.getOldStep().equals("basicTabCat")) {
      if (category.isCustomDivision()) {
        return "customTabCat";
      } else {
        return "summaryTabCat";
      }

    } else if (event.getOldStep().equals("summaryTabCat")
        && event.getNewStep().equals("customTabCat")) {
      if (category.isCustomDivision()) {
        return "customTabCat";
      } else {
        return "basicTabCat";
      }
    } else if (event.getOldStep().equals("customTabCat")
        && event.getNewStep().equals("summaryTabCat")) {
      double sum = 0.0;
      for (UserGroupCategoryDTO ugc : ugcList) {
        sum += ugc.getPercent().doubleValue();
      }
      if (sum == 1.0) {
        return "summaryTabCat";
      }
      // else if (sum == 0.99){
      // sum=Math.ceil(sum*10)/10;
      // }
      else {
        FacesMessage message =
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "The total sum of the percents has to be equal 1.0", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "customTabCat";
      }
    }

    return event.getNewStep();
  }


  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

  public String getReturnPage() {
    return returnPage;
  }

  public void setReturnPage(String returnPage) {
    this.returnPage = returnPage;
  }

}
