package com.smartwg.core.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

import org.joda.time.DateTime;

import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;

@Named
@Singleton
public class SmartWG {

  /**
   * default empty constructor
   */

  private Set<UserDTO> onlineUsers;
  private Date costDivisionOverviewSelectedDate;
  private Date workDistribOverviewSelectedDate;
  private String current_locale;
  private HashMap<String, Double> currentBalance = new HashMap<String, Double>();
  private static ResourceBundle MESSAGE_BUNDLE = ResourceBundle.getBundle(Constants.I18N_MESSAGE_BUNDLE_PATH);
  private static ResourceBundle VALIDATION_MESSAGE_BUNDLE= ResourceBundle.getBundle(Constants.I18N_VALIDATION_BUNDLE_PATH);
  public SmartWG() {}

  @PostConstruct
  private void init() {
    if (onlineUsers == null) {
      this.onlineUsers = new HashSet<UserDTO>();
    }
    if (costDivisionOverviewSelectedDate == null) {
      costDivisionOverviewSelectedDate = DateTime.now().toDate();
    }
    if (workDistribOverviewSelectedDate == null) {
      workDistribOverviewSelectedDate = DateTime.now().toDate();
    }
  }

  public void changeLanguage(){
    ResourceBundle.clearCache();
   SmartWG.MESSAGE_BUNDLE= ResourceBundle.getBundle(Constants.I18N_MESSAGE_BUNDLE_PATH, new Locale(current_locale));
   SmartWG.VALIDATION_MESSAGE_BUNDLE= ResourceBundle.getBundle(Constants.I18N_VALIDATION_BUNDLE_PATH,new Locale(current_locale));
  }
  public double getCurrentBalance(String userName) {

    if (currentBalance.containsKey(userName)) {
      return currentBalance.get(userName);
    }

    return 0;
  }

  public boolean isDateBigger(Date date) {
    return date.compareTo(DateTime.now().toDate()) >= 0;
  }

  public Date getCostDivisionOverviewSelectedDate() {
    return this.costDivisionOverviewSelectedDate;
  }

  public void setCostDivisionOverviewSelectedDate(Date date) {
    this.costDivisionOverviewSelectedDate = date;
  }
  

  public String getCurrent_locale() {
    return current_locale;
  }

  public void setCurrent_locale(String current_locale) {
    this.current_locale = current_locale;
  }

 
  public static ResourceBundle getMessageBundle() {
    return MESSAGE_BUNDLE;
  }
  
  public static ResourceBundle getValidationBundle(){
    return VALIDATION_MESSAGE_BUNDLE;
  }

  public Set<UserDTO> getOnlineUsers() {
    return onlineUsers;
  }



  public Date getWorkDistribOverviewSelectedDate() {
    return workDistribOverviewSelectedDate;
  }


  public void setWorkDistribOverviewSelectedDate(Date workDistribOverviewSelectedDate) {
    this.workDistribOverviewSelectedDate = workDistribOverviewSelectedDate;
  }


  public HashMap<String, Double> getCurrentBalance() {
    return currentBalance;
  }


  public void setCurrentBalance(HashMap<String, Double> currentBalance) {
    this.currentBalance = currentBalance;
  }


  public void setOnlineUsers(Set<UserDTO> onlineUsers) {
    this.onlineUsers = onlineUsers;
  }

  public boolean isOnline(UserDTO user) {
    return onlineUsers.contains(user);
  }

  public void addOnlineUser(UserDTO user) {
    this.onlineUsers.add(user);
  }

  public boolean removeOnlineUser(UserDTO user) {
    return onlineUsers.remove(user);
  }



}
