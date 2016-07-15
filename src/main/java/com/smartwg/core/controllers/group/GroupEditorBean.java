package com.smartwg.core.controllers.group;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.CountryFacade;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;

@Named("groupEditor")
@Scope("view")
public class GroupEditorBean {
  @Inject
  private GroupFacade facade;
  @Inject
  private CountryFacade countryFacade;
  @Inject
  private NavigationBean navigation;
  @Inject
  private UserBean userBean;
  private List<CountryDTO> countries;
  private GroupDTO group;


  @PostConstruct
  public void init() {
    this.group = new GroupDTO();
    this.countries = countryFacade.getAllCountries();
  }

  public List<CountryDTO> getCountries() {
    return countries;
  }

  public void setCountries(List<CountryDTO> countries) {
    this.countries = countries;
  }

  public GroupDTO getGroup() {
    return group;
  }

  public void setGroup(GroupDTO group) {
    this.group = group;
  }

  public String createGroup() {
    facade.saveGroup(group, userBean.getCurrentUser());
    return navigation.getPageHome();
  }



}
