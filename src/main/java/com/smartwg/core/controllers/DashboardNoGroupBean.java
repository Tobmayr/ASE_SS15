package com.smartwg.core.controllers;


import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.CountryFacade;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Matthias Hoellthaler (mh)
 */
@Named("dashboardNoGroup")
@Scope("view")
public class DashboardNoGroupBean {

  @Inject
  private UserBean userBean;

  @Inject
  private GroupFacade groupFacade;
  @Inject
  private CountryFacade countryFacade;
  @Inject
  private NavigationBean navigation;

  private List<GroupDTO> groups;
  private int index;
  private GroupDTO newGroup;
  private List<CountryDTO> countries;
  private String searchParam;
  private ResourceBundle valMs = SmartWG.getValidationBundle();

  @PostConstruct
  public void init() {
    newGroup = new GroupDTO();
    countries = countryFacade.getAllCountries();
  }

  public void searchGroups() {
    System.out.println("Search with param " + searchParam);
    groups = groupFacade.searchGroups(searchParam);

  }

  public void joinGroup(GroupDTO groupDTO) {
    UserDTO user = userBean.getCurrentUser();

    groupFacade.joinGroup(groupDTO, user);
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
        valMs.getString("info.joinrequest.send"));
  }

  public void joinGroup(Group group) {
    GroupDTO group1 = new GroupDTO();
    group1.setId(group.getId());
    joinGroup(group1);
  }

  public String createGroup() {
    groupFacade.saveGroup(newGroup, userBean.getCurrentUser());
    return navigation.getPageHome();
  }


  public NavigationBean getNavigation() {
    return navigation;
  }

  public void setNavigation(NavigationBean navigation) {
    this.navigation = navigation;
  }



  public List<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDTO> groups) {
    this.groups = groups;
  }

  public GroupDTO getNewGroup() {
    return newGroup;
  }

  public void setNewGroup(GroupDTO newGroup) {
    this.newGroup = newGroup;
  }

  public List<CountryDTO> getCountries() {
    return countries;
  }

  public void setCountries(List<CountryDTO> countries) {
    this.countries = countries;
  }

  public String getSearchParam() {
    return searchParam;
  }

  public void setSearchParam(String searchParam) {
    this.searchParam = searchParam;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }



}
