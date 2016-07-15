package com.smartwg.core.controllers;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.util.Constants;

/**
 * This Bean contains the Navigation-route constants and a flag to determine whether mobile or
 * desktop route should be returned
 * 
 * @author ASE Group 04
 */
@Named("navigation")
@Scope("session")
public class NavigationBean {
  private static final Logger logger = LoggerFactory.getLogger(NavigationBean.class);
  private final static String MOBILE_PREFIX = "/m";
  private final static String PAGE_INDEX = "/public/index";
  private final static String PAGE_HOME = "/pages/main";

  private final static String PAGE_REGISTER = "/public/user/register";
  private final static String PAGE_GTC = "/public/user/gtc";
  private final static String PAGE_FORGOTPASS = "/public/user/forgotPW";
  private final static String PAGE_RESET_PASS = "/public/user/resetPW";

  private final static String PAGE_NEW_BILL = "/pages/bill/newBill";
  private final static String PAGE_BILL_OVERVIEW = "/pages/bill/overview";
  private final static String PAGE_SHOW_BILL = "/pages/bill/showBill";

  private final static String PAGE_NEW_ACTIVITY = "/pages/activities/newActivity";
  private final static String PAGE_ACTIVITY_OVERVIEW = "/pages/activities/activityList";
  private final static String PAGE_CALENDAR = "/pages/activities/calendar";

  private final static String PAGE_BILL_PHOTO = "/pages/bill/newImage";

  private final static String PAGE_ADD_GROUP = "/pages/group/addGroup";
  private final static String PAGE_GROUP_LIST = "/pages/group/groupList" + Constants.PAGE_REDIRECT
      + "&list=true";
  private final static String PAGE_GROUP_ADMINISTRATION = "/pages/group/groupAdministration";
  private final static String PAGE_SEARCH_GROUP = "/pages/group/search";

  private final static String PAGE_NEW_ABSENCE = "/pages/absence/newAbsence";
  private final static String PAGE_GROUP_ABSENCES = "/pages/absence/groupAbsences";
  private final static String PAGE_USER_ABSENCES = "/pages/absence/userAbsences";

  private final static String PAGE_GROUP_SHOPPING_LISTS = "/pages/shoppinglist/groupShoppingLists";
  private final static String PAGE_NEW_SHOPPING_LIST = "/pages/shoppinglist/newShoppingList";
  private final static String PAGE_SHOPPING_LIST_OVERVIEW =
      "/pages/shoppinglist/shoppingListOverview";
  private final static String PAGE_USER_SHOPPING_LISTS = "/pages/shoppinglist/userShoppingLists";

  private final static String PAGE_USER_LIST = "/pages/user/userList";
  private final static String PAGE_USER_EDIT = "/pages/user/editUser";
  private final static String PAGE_USER_SHOW = "/pages/user/showUser";

  private final static String PAGE_COST_DIVISION_OVERVIEW =
      "/pages/costDivision/costDivisionOverview";
  private final static String PAGE_COST_DIVISION_DETAILS =
      "/pages/costDivision/costDivisionDetails";

  private final static String PAGE_GROUP_PAYMENTS = "/pages/costDivision/groupPayments";
  private final static String PAGE_USER_PAYMENTS = "/pages/costDivision/userPayments";

  private final static String PAGE_WORK_DISTRIBUTION_OVERVIEW =
      "/pages/workDistrib/workDistribOverview";
  private final static String PAGE_WORK_DISTRIBUTION_DETAILS =
      "/pages/workDistrib/workDistribDetails";
  private final static String PAGE_RATINGS = "/pages/workDistrib/ratings";

  private final static String PAGE_NEW_CATEGORY = "/pages/category/newCategory";
  private final static String PAGE_CATEGORY_OVERVIEW = "/pages/category/categoryOverview";
  private final static String PAGE_SHOW_CATEGORY = "/pages/category/showCategory";

  private final static String PAGE_STATISTICS = "/pages/statistics/statisticsSetup";
  private final static String PAGE_DASHBOARD_FRESH_USER = "/pages/mainNoGroup";
  private final static String PAGE_ABOUT = "/public/about";

  private final static String PAGE_ACTIVITY_PLANER = "/pages/workDistrib/planer";
  private final static String PAGE_GROUP_SEARCH = "/pages/group/search";

  private static boolean mobileAcces = false;
  private static String prefix = "";

  @PostConstruct
  public void init() {
    logger.info("Bean created");
  }

  public static boolean isMobileAcces() {
    return mobileAcces;
  }

  public static void setMobileAcces(boolean mobileAcces) {
    if (mobileAcces)
      prefix = MOBILE_PREFIX;
    else
      prefix = "";
    NavigationBean.mobileAcces = mobileAcces;
  }

  public String getPageUserShow() {
    return prefix + PAGE_USER_SHOW;
  }

  public String getPageIndex() {
    return prefix + PAGE_INDEX;
  }

  public String getPageHome() {
    return prefix + PAGE_HOME;
  }

  public String getPageRegister() {
    return prefix + PAGE_REGISTER;
  }

  public String getPageGtc() {
    return prefix + PAGE_GTC;
  }

  public String getPageForgotpass() {
    return prefix + PAGE_FORGOTPASS;
  }

  public String getPageNewBill() {
    return prefix + PAGE_NEW_BILL;
  }

  public String getPageBillOverview() {
    return prefix + PAGE_BILL_OVERVIEW;
  }

  public String getPageAddGroup() {
    return prefix + PAGE_ADD_GROUP;
  }

  public String getPageGroupList() {
    return prefix + PAGE_GROUP_LIST;
  }

  public String getPageGroupShoppingLists() {
    return prefix + PAGE_GROUP_SHOPPING_LISTS;
  }

  public String getPageNewShoppingList() {
    return prefix + PAGE_NEW_SHOPPING_LIST;
  }

  public String getPageShoppingListOverview() {
    return prefix + PAGE_SHOPPING_LIST_OVERVIEW;
  }

  public String getPageUserShoppingLists() {
    return prefix + PAGE_USER_SHOPPING_LISTS;
  }

  public String getPageUserList() {
    return prefix + PAGE_USER_LIST;
  }

  public String getPageGroupAdministration() {
    return prefix + PAGE_GROUP_ADMINISTRATION;
  }

  public String getPageUserEdit() {
    return prefix + PAGE_USER_EDIT;
  }

  public String getPageBillPhoto() {
    return MOBILE_PREFIX + PAGE_BILL_PHOTO;
  }

  public String getPageSearchGroup() {
    return prefix + PAGE_SEARCH_GROUP;
  }

  public String getPageNewActivity() {
    return prefix + PAGE_NEW_ACTIVITY;
  }

  public String getPageActivityOverview() {
    return prefix + PAGE_ACTIVITY_OVERVIEW;
  }

  public String getPageCalendar() {
    return prefix + PAGE_CALENDAR;
  }

  public String getPageShowBill() {
    return prefix + PAGE_SHOW_BILL;
  }

  public String getPageNewAbsence() {
    return prefix + PAGE_NEW_ABSENCE;
  }

  public String getPageUserAbsences() {
    return prefix + PAGE_USER_ABSENCES;
  }

  public String getPageGroupAbsences() {
    return prefix + PAGE_GROUP_ABSENCES;
  }

  public String getPageNewCategory() {
    return prefix + PAGE_NEW_CATEGORY;
  }

  public String getPageCategoryOverview() {
    return prefix + PAGE_CATEGORY_OVERVIEW;
  }


  public String getPageCostDivisionDetails() {
    return prefix + PAGE_COST_DIVISION_DETAILS;
  }

  public String getPageCostDivisionOverview() {
    return prefix + PAGE_COST_DIVISION_OVERVIEW;
  }


  public String getPageGroupPayments() {
    return prefix + PAGE_GROUP_PAYMENTS;
  }


  public String getPageUserPayments() {
    return prefix + PAGE_USER_PAYMENTS;
  }

  public String getPageShowCategory() {
    return prefix + PAGE_SHOW_CATEGORY;
  }


  public String getPageStatistics() {
    return prefix + PAGE_STATISTICS;
  }

  public String getPageResetPass() {
    return prefix + PAGE_RESET_PASS;
  }

  public String getPageWorkDistributionOverview() {
    return prefix + PAGE_WORK_DISTRIBUTION_OVERVIEW;
  }

  public String getPageWorkDistributionDetails() {
    return prefix + PAGE_WORK_DISTRIBUTION_DETAILS;
  }

  public String getPageRatings() {
    return prefix + PAGE_RATINGS;
  }

  public String getPageDashboardFreshUser() {
    return prefix + PAGE_DASHBOARD_FRESH_USER;
  }

  public String getPageAbout() {
    return prefix + PAGE_ABOUT;
  }

  public String getPageActivityPlaner() {
    return prefix + PAGE_ACTIVITY_PLANER;
  }

  public String getPageGroupSearch() {
    return prefix + PAGE_GROUP_SEARCH;
  }
}
