package com.smartwg.core.util;

/**
 * This Util class provides static methods for often used String constants such as Querynames or
 * resource bundle paths
 * 
 * @author ASE 04 Group
 * 
 * 
 *
 */
public class Constants {


  /*
   * 
   * Resource Bundles
   */
  public static final String I18N_MESSAGE_BUNDLE_PATH = "com/smartwg/i18n/messages";;
  public static final String I18N_VALIDATION_BUNDLE_PATH = "com/smartwg/i18n/validationMessages";
  /*
   * NamedQueries
   */
  public static final String FIND_ALL_USERS = "findAllUsers";
  public static final String QUERY_FIND_CONFIRMED_USER = "findConfirmedUser";
  public static final String QUERY_FIND_USER_NAME_EMAIL = "findUserByNameEmail";
  public static final String QUERY_FIND_USER_BY_EMAIL = "findUsersByMail";
  public static final String QUERY_FIND_SHOPPINGLISTS_GROUP_STATE =
      "findShoppingListsByGroupIdAndState";
  public static final String QUERY_FIND_SHOPPINGLISTS_USERID = "findShoppingListsByUserId";
  public static final String QUERY_FIND_SHOPPINGLIST_POSITION_ID =
      "findShoppingListPositionsByListId";
  public static final java.lang.String QUERY_FIND_SHOPPINGLISTS_ASSIGNED_TO_USER =
      "findShoppingListsAssignedToUser";
  public static final String QUERY_FIND_SHOPPINGLIST_ID = "findShoppingListsById";
  public static final String QUERY_ALL_CATEGORIES = "findAllCategories";
  public static final String QUERY_COLLECTIVE_BILLS_TIMESPAN = "findCollectiveBillsByTimespan";
  public static final String QUERY_COLLECTIVE_BILLS_WITH_COST_ENTRIES_TIMESPAN =
      "findCollectiveBillsWithCostEntriesByTimespan";
  public static final String QUERY_PAYMENT_USERS_BY_PAYMENT_ID = "findPaymentUsersByPaymentId";
  public static final String QUERY_UNCONFIRMED_PAYMENT_USERS_BY_USER_ID =
      "findUnconfirmedPaymentUsersByUserId";
  public static final String QUERY_PAYMENT_USERS_BY_USER_ID = "findPaymentUsersByUserId";
  public static final String QUERY_COSTENTRIES_BY_ID = "findCostEntriesbyId";
  public static final String QUERY_PRIVATE_BILLS_TIMESPAN = "findPrivateBillsByTimespan";
  public static final String QUERY_FIND_BILL_BY_ID = "findBillById";
  public static final String QUERY_FIND_ACTIVITY_BY_ID = "findActivityById";
  public static final String QUERY_FIND_ASSIGNED_ACTIVITES_TIMESPAN =
      "findAssignedActivitesTimespan";
  public static final String QUERY_ALL_GROUPS = "findAllGroups";
  public static final String QUERY_ALL_GROUPS_BY_USER = "findAllGroupsByUserId";
  public static final String QUERY_FIND_USERS_BY_GROUP = "findUsersByGroupID";
  public static final String QUERY_FIND_COUNTRIES = "findAllCountries";
  public static final String QUERY_FIND_ABSENCES_GROUP = "findAbsencesByGroupId";
  public static final String QUERY_FIND_ABSENCES_USERID = "findAbsencesByUserId";
  public static final String QUERY_FIND_ABSENCE_ID = "findAbsenceById";
  public static final String QUERY_FIND_CATEGORY_BY_GROUP = "findCategoriesByGroupAndDefault";
  public static final String QUERY_FIND_UGC_BY_CATEGORY = "findUserGroupCategoryByCategory";
  public static final String QUERY_FIND_USER_NOTIFICATIONS = "findUserNotifications";
  public static final String QUERY_FIND_USER_EMAILS_BY_GROUP_ID = "findUserEmailsByGroupId";
  public static final String QUERY_FIND_BILLS_BY_SHOP = "findBillsByShop";
  public static final String QUERY_BILLS_TIMESPAN_BY_SHOP = "findBillsByTimespanByShop";
  public static final String QUERY_COSTENTRIES_BY_CATEGORY = "findCostEntriesByCategory";
  public static final String QUERY_BILLS_TIMESPAN_BY_USER = "findBillsByTimespanByUser";
  public static final String QUERY_COSTENTRIES_TIMESPAN_BY_CATEGORY_BY_USER =
      "findCostEntriesByTimespanByCategoryByUser";
  public static final String QUERY_COSTENTRIES_TIMESPAN_BY_CATEGORY =
      "findCostEntriesByTimespanByCategory";
  public static final String QUERY_BILLS_TIMESPAN_BY_SHOP_BY_USER =
      "findBillsByTimespanByShopByUser";
  public static final String QUERY_FIND_USER_ACTIVITY = "findUserActivityByUserIdAndActivityId";
  public static final String QUERY_ACTIVITY_BY_GROUP_ID = "findActivityByGroupId";
  public static final String QUERY_RATINGS_BY_ACTIVITY_ID = "findRatingsByActivityId";
  public static final String QUERY_ACTIVITIES_BY_TIMESPAN = "findActivitiesByTimespan";
  public static final String QUERY_ACTIVITIES = "findAllActivities";
  public static final String QUERY_ACTIVITES_PLANED = "findPlanedActivities";
  public static final String QUERY_ALL_ADMINS = "findAllAdmins";
  public static final String QUERY_FIND_GROUP_MEMBERS_EMAILS = "findGroupMembersEmails";
  /*
   * Misc.
   */
  public static final String PAGE_REDIRECT = "?faces-redirect=true";
  /*
   * Regex-Pattern Constants (used in the Recognition-Pipeline)
   */
  public static final String PATTERN_COSTENTRY =
      "[\\wäÄüÜöÖß][\\w\\s.,-/äÄüÜöÖß]+\\s(([A-Za-z]\\s[0-9]{1,5}[.,][0-9]{2})|([0-9]{1,5}[.,][0-9]{2}\\s[A-Za-z]))";
  public static final String PATTERN_AMOUNT = "[0-9]{1,5}[.,][0-9]{2}";
  public static final String PATTERN_DATE_DELIMITER = "[- /.]";
  public static final String PATTERN_DATE = "(0[1-9]|[12][0-9]|3[01])" + PATTERN_DATE_DELIMITER
      + "(0[1-9]|1[012])" + PATTERN_DATE_DELIMITER + "(19|20)\\d\\d";
  /*
   * Testdata directories
   */
  public static final String DIR_TEST_DATA_IMAGES = "test-data/ir/images/";
  public static final String DIR_TEST_DATA_RESULTSTRING = "test-data/ir/result/";
  public static final String DIR_TEST_DATA_PAYMENTLOG = "test-data/paylog/";

  private Constants() {}


}
