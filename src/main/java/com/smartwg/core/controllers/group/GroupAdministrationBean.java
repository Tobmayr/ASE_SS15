package com.smartwg.core.controllers.group;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.CountryFacade;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.facades.UserGroupFacade;
import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.HttpUtil;
import com.smartwg.core.util.PrimefacesUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

@Named("groupAdministration")
@Scope("view")
public class GroupAdministrationBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupAdministrationBean.class);
  private ResourceBundle valMs = SmartWG.getValidationBundle();
  @Inject
  private GroupFacade facade;
  @Inject
  private NavigationBean navigation;
  @Inject
  private UserBean userBean;
  @Inject
  private UserFacade userFacade;
  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private CountryFacade countryFacade;
  @Inject
  private ShopFacade shopFacade;

  @Inject
  private UserGroupFacade userGroupFacade;
  private GroupDTO group;

  private List<UserDTO> groupusers;

  private List<UserDTO> filteredUsers;

  private List<CountryDTO> countries;

  private List<CategoryDTO> categories;

  private List<ShopDTO> shops;

  private List<CategoryDTO> filteredCategories;
  private List<GroupDTO> adminGroups;

  private ShopDTO newShop;
  private CategoryDTO selectedCategory;
  private boolean editable;
  private int tabindex = 0;
  private String userSearch;

  public GroupAdministrationBean() {}

  @PostConstruct
  public void init() {
    if (!userBean.getCurrentUserGroup().getRole().equals(Role.ADMIN)) {
      ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
      try {
        context.redirect(HttpUtil.getBaseURL() + navigation.getPageHome() + ".jsf");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    String tab = requestParameterMap.get("tab");


    if (tab != null)
      tabindex = Integer.valueOf(tab);
    adminGroups = facade.getAdministratedGroups(userBean.getCurrentUser().getId());


    if (adminGroups != null && !adminGroups.isEmpty()) {
      group = adminGroups.get(0);
      loadGroupDetails();
    }

    countries = countryFacade.getAllCountries();
    newShop = new ShopDTO();
    newShop.setGroupId(group.getId());


  }


  public boolean isAdmin(UserDTO user) {
    for (UserGroupDTO ug : group.getUserGroups()) {
      if (user.getId().equals(ug.getUserId()) && ug.getRole().equals(Role.ADMIN)) {
        return true;
      }
    }
    return false;
  }

  private int getAdmins() {
    int count = 0;
    for (UserGroupDTO ug : group.getUserGroups()) {
      if (ug.getRole().equals(Role.ADMIN)) {
        count++;
      }
    }
    return count;
  }

  private void loadGroupDetails() {
    groupusers = userFacade.findUsersByGroupID(group.getId());
    LOGGER.info("Groupid= " + group.getId());
    categories = categoryFacade.findByGroup(group.getId());
    LOGGER.info("CategorySize: " + categories.size());
    Integer countryId = null;
    if (group.getCountry() != null) {
      countryId = group.getCountry().getId();
    }
    shops = shopFacade.findByGroup(group.getId(), countryId);
  }

  public void groupChanged(AjaxBehaviorEvent event) {
    LOGGER.info("Group changed");
    loadGroupDetails();
  }

  public void editGroup() {
    if (!editable) {
      editable = true;

    } else {
      facade.saveGroup(group, null);
      editable = false;
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
          valMs.getString("info.groupedit.successfull"));
    }

  }

  public void editShop(ShopDTO shop) {
    if (shop.getName() == null || shop.getName().isEmpty()) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.shopname.required"));
      return;
    } else if (nameExists(shop.getName())) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.shopname.unique"));
      return;
    }
    boolean add = shop.getId() == null;
    LOGGER.info("asdasdasd" + shop.getGroupId());
    shopFacade.createShop(shop);
    if (add)
      shops.add(shop);

  }

  private boolean nameExists(String name) {
    for (ShopDTO s : shops) {
      if (s.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public void discardChanges() {
    editable = false;
    group = facade.findById(userBean.getCurrentUserGroup().getGroupId());
  }

  public String viewUser(final UserDTO user, final String returnPage) {
    return navigation.getPageUserShow() + Constants.PAGE_REDIRECT + "&UserId=" + user.getId()
        + "&returnPage=" + returnPage;

  }

  public void giveAdminRights(UserDTO user) {
    for (UserGroupDTO usergroup : group.getUserGroups()) {
      if (usergroup.getUserId() == user.getId()) {
        if (usergroup.getRole() == Role.USER) {
          usergroup.setRole(Role.ADMIN);
        } else {
          if (getAdmins() > 1) {
            usergroup.setRole(Role.USER);
          } else {
            PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
                valMs.getString("error.changeAdmin.onerequired"));
            return;
          }

        }
        userFacade.updateUserGroup(usergroup);
        groupusers = userFacade.findUsersByGroupID(group.getId());
        return;
      }
    }
  }

  public boolean isDeleted(UserDTO user) {
    for (UserGroupDTO ug : user.getGroups()) {
      if (ug.getGroupId().equals(group.getId())) {
        return ug.isDeleted();
      }
    }
    return false;
  }

  public String deleteGroup() {

    String gname = group.getName();
    facade.deleteGroup(group);
    userBean.setGroupsOfCurrentUser(facade.getAllGroupsByUserID(userBean.getCurrentUser().getId()));
    UserDTO currentUser = userFacade.findById(userBean.getCurrentUser().getId());
    if (currentUser.getGroups() != null && !currentUser.getGroups().isEmpty()) {
      userBean.setCurrentUser(currentUser);
      UserGroupDTO currentUserGroup = currentUser.getGroups().get(0);
      userBean.setCurrentUserGroup(currentUserGroup);
      userBean.setUsersOfCurrentGroup(userFacade.findUsersByGroupID(currentUserGroup.getGroupId()));
      userBean.setGroupsOfCurrentUser(facade.getAllGroupsByUserID(currentUser.getId()));
      userBean.setCurrentGroup(facade.findById(currentUserGroup.getGroupCountryId()));
      return navigation.getPageHome() + Constants.PAGE_REDIRECT + "&msg=info.group.deleted"
          + "&group=" + gname;
    } else {
      userBean.setCurrentGroup(null);
      userBean.setCurrentUserGroup(null);
      userBean.setGroupsOfCurrentUser(null);
      userBean.setCurrentGroup(null);
      userBean.setUsersOfCurrentGroup(null);
      return navigation.getPageDashboardFreshUser() + Constants.PAGE_REDIRECT
          + "&msg=info.group.deleted" + "&group=" + gname;
    }

  }

  public void addUser() {
    if (userSearch != null) {
      UserDTO user = userFacade.findByUserNameOrMail(userSearch);
      if (user == null) {
        PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
            valMs.getString("error.addUser.exist"));
        return;
      } else {
        if (groupusers.contains(user)) {
          String message =
              valMs.getString("error.addUser.member").replace(":user", user.getUserName());
          PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR, message);
          return;
        }
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setGroupId(group.getId());
        userGroupDTO.setUserId(user.getId());
        userGroupDTO.setRole(Role.USER);
        if (userGroupFacade.findUserGroupByUsernameGroupId(user.getUserName(), group.getId()) == null) {
          userGroupFacade.createUserGroup(userGroupDTO);
        } else {
          userGroupDTO.setDeleted(false);
          userGroupDTO.setLeaveDate(null);
          userFacade.updateUserGroup(userGroupDTO);
        }
        String message =
            valMs.getString("info.useradded.succes").replace(":user", user.getUserName());
        PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO, message);
        userFacade.sendUserAddedInfoMail(user, group);
        loadGroupDetails();
      }
    }

  }

  public void removeUser(UserDTO user) {
    List<UserGroupDTO> usergroups = user.getGroups();
    for (UserGroupDTO usergroup : usergroups) {
      if (usergroup.getGroupId() == userBean.getCurrentUserGroup().getGroupId()) {
        if (usergroup.getRole().equals(Role.ADMIN)) {
          PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
              valMs.getString("error.deleteUser.admin"));
          return;
        }
        userFacade.removeUserGroup(usergroup);

        usergroup.setDeleted(true);
        groupusers = userFacade.findUsersByGroupID(group.getId());


      }
    }
  }

  public String removeShop(ShopDTO shop) {
    shopFacade.removeShop(shop);
    return navigation.getPageGroupAdministration() + "?tab=2";
  }

  public void setfixedCost(CategoryDTO category) {
    categoryFacade.saveCategory(category);
  }

  public void setcustomDivision(CategoryDTO category) {
    if (category.isCustomDivision())
      category.setCustomDivision(false);
    else
      category.setCustomDivision(true);
    categoryFacade.saveCategory(category);
  }

  public void removeCategory(CategoryDTO category) {

    category.setDeleted(true);
    categoryFacade.deleteCategory(category);

  }

  public GroupDTO getGroup() {
    return group;
  }

  public void setGroup(GroupDTO group) {
    this.group = group;
  }

  public List<UserDTO> getGroupusers() {
    return groupusers;
  }

  public void setGroupusers(List<UserDTO> groupusers) {
    this.groupusers = groupusers;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

  public List<UserDTO> getFilteredUsers() {
    return filteredUsers;
  }

  public void setFilteredUsers(List<UserDTO> filteredUsers) {
    this.filteredUsers = filteredUsers;
  }

  public List<CategoryDTO> getFilteredCategories() {
    return filteredCategories;
  }

  public void setFilteredCategories(List<CategoryDTO> filteredCategories) {
    this.filteredCategories = filteredCategories;
  }

  public CategoryDTO getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(CategoryDTO selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  public List<ShopDTO> getShops() {
    return shops;
  }

  public void setShops(List<ShopDTO> shops) {
    this.shops = shops;
  }

  public List<CountryDTO> getCountries() {
    return countries;
  }

  public void setCountries(List<CountryDTO> countries) {
    this.countries = countries;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public int getTabindex() {
    return tabindex;
  }

  public void setTabindex(int tabindex) {
    this.tabindex = tabindex;
  }

  public ShopDTO getNewShop() {
    return newShop;
  }

  public void setNewShop(ShopDTO newShop) {
    this.newShop = newShop;
  }

  public List<GroupDTO> getAdminGroups() {
    return adminGroups;
  }

  public void setAdminGroups(List<GroupDTO> adminGroups) {
    this.adminGroups = adminGroups;
  }

  public String getUserSearch() {
    return userSearch;
  }

  public void setUserSearch(String userSearch) {
    this.userSearch = userSearch;
  }
}
