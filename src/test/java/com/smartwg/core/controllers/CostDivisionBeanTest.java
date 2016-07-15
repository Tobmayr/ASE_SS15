package com.smartwg.core.controllers;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.ContextMocker;
import com.smartwg.core.controllers.costDivision.CostDivisionBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.PaymentFacade;
import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.services.PaymentLogConversionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Max Schwarzfischer (ms)
 */

@Test
public class CostDivisionBeanTest {

  @InjectMocks
  private CostDivisionBean costDivisionBean;

  @Mock
  private UserBean userBean;

  @Mock
  private BillFacade billFacade;

  @Mock
  private AbsenceFacade absenceFacade;

  @Mock
  private PaymentLogConversionService paymentLogConversionService;

  @Mock
  private PaymentFacade paymentFacade;

  @Mock
  private UserGroupCategoryFacade userGroupCategoryFacade;

  @Mock
  private SmartWG smartWG;

  private UserDTO userDTO_Max, userDTO_Tina, userDTO_Luna;
  private UserGroupDTO userGroupDTO_Max, userGroupDTO_Tina, userGroupDTO_Luna;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      // Mock objects needed for FacesContext
      Map<String, Object> session = new HashMap<>();
      ExternalContext ext = Mockito.mock(ExternalContext.class);
      when(ext.getSessionMap()).thenReturn(session);
      when(context.getExternalContext()).thenReturn(ext);

      // Mock objects needed for payments
      when(paymentLogConversionService.serialize(anyObject())).thenReturn(null);
      when(paymentFacade.createOrUpdatePayment(anyObject())).thenReturn(1);
      when(paymentFacade.findPaymentUsersByUserId(anyInt())).thenReturn(new ArrayList<>());
      when(smartWG.getCostDivisionOverviewSelectedDate()).thenReturn(DateTime.now().toDate());

      // Mock a group of three users where user with ID 1 is current user and also Admin
      userDTO_Max = new UserDTO();
      userDTO_Max.setId(1);
      userDTO_Max.setUserName("Max");

      userDTO_Tina = new UserDTO();
      userDTO_Tina.setId(2);
      userDTO_Tina.setUserName("Tina");

      userDTO_Luna = new UserDTO();
      userDTO_Luna.setId(3);
      userDTO_Luna.setUserName("Luna");

      List<UserDTO> userDTOList = new ArrayList<>();
      userDTOList.add(userDTO_Max);
      userDTOList.add(userDTO_Tina);
      userDTOList.add(userDTO_Luna);

      when(userBean.getUsersOfCurrentGroup()).thenReturn(userDTOList);
      when(userBean.getCurrentUser()).thenReturn(userDTO_Max);

      GroupDTO groupDTO = new GroupDTO();
      groupDTO.setId(1);
      CountryDTO countryDTO = new CountryDTO();
      CurrencyDTO currencyDTO = new CurrencyDTO();
      currencyDTO.setIsoCode("EUR");
      countryDTO.setCurrency(currencyDTO);
      groupDTO.setCountry(countryDTO);

      userGroupDTO_Max = new UserGroupDTO();
      userGroupDTO_Max.setGroupId(groupDTO.getId());
      userGroupDTO_Max.setUserId(userDTO_Max.getId());
      userGroupDTO_Max.setJoinDate(DateTime.now().minusMonths(1).toDate());
      userGroupDTO_Max.setRole(Role.ADMIN);
      userDTO_Max.setGroups(new ArrayList<UserGroupDTO>() {
        {
          add(userGroupDTO_Max);
        }
      });

      userGroupDTO_Tina = new UserGroupDTO();
      userGroupDTO_Tina.setGroupId(groupDTO.getId());
      userGroupDTO_Tina.setUserId(userDTO_Tina.getId());
      userGroupDTO_Tina.setJoinDate(DateTime.now().minusMonths(1).toDate());
      userGroupDTO_Tina.setRole(Role.USER);
      userDTO_Tina.setGroups(new ArrayList<UserGroupDTO>() {
        {
          add(userGroupDTO_Tina);
        }
      });

      userGroupDTO_Luna = new UserGroupDTO();
      userGroupDTO_Luna.setGroupId(groupDTO.getId());
      userGroupDTO_Luna.setUserId(userDTO_Luna.getId());
      userGroupDTO_Luna.setJoinDate(DateTime.now().minusMonths(1).toDate());
      userGroupDTO_Luna.setRole(Role.USER);
      userDTO_Luna.setGroups(new ArrayList<UserGroupDTO>() {
        {
          add(userGroupDTO_Luna);
        }
      });

      when(userBean.getCurrentUserGroup()).thenReturn(userGroupDTO_Max);
      when(userBean.getCurrentGroup()).thenReturn(groupDTO);

      // Mock Bills (this mock needs to be overwritten with specific data in tests)
      when(billFacade.getBillsWithCostEntriesBetweenTimespan(anyObject(), anyObject(), anyInt()))
          .thenReturn(new ArrayList<>());

      costDivisionBean.init();
    } finally {
      context.release();
    }
  }

  public void isCurrentUserAdmin_shouldReturnTrue() {
    Assert.assertTrue(costDivisionBean.isCurrentUserAdmin());
  }

  public void getUsers_shouldReturnUserBeanGetUsersOfCurrentGroup() {
    Assert.assertEquals(costDivisionBean.getUsers(), userBean.getUsersOfCurrentGroup());
  }

  public void getPayment_getId_shouldNotReturnNullAfterSaveButtonClicked() {
    Assert.assertNotEquals(costDivisionBean.getPayment(), null);
    costDivisionBean.getPayment().setId(null);
    Assert.assertEquals(costDivisionBean.getPayment().getId(), null);
    costDivisionBean.saveButtonClicked(false);
    Assert.assertNotEquals(costDivisionBean.getPayment().getId(), null);
  }

  public void saveButtonClicked_shouldAddMessageToFaceContextIfNotifyIsTrue() {
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      costDivisionBean.saveButtonClicked(true);
      verify(context).addMessage(anyString(), anyObject());
    } finally {
      context.release();
    }
  }

  public void saveButtonClicked_shouldNotAddMessageToFaceContextIfNotifyIsFalse() {
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      costDivisionBean.saveButtonClicked(false);
      verifyZeroInteractions(context);
    } finally {
      context.release();
    }
  }

  public void calculatePaymentUsers_shouldNotCreatePaymentUsersIfAllUsersHaveBillsWithSameAmount() {
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      CategoryDTO categoryDTO = new CategoryDTO(1, "Lebensmittel", false, false);

      // Create a bill for user with Id = 1
      BillDTO billDTO_Max = new BillDTO();
      billDTO_Max.setId(1);
      billDTO_Max.setDate(DateTime.now().toDate());
      billDTO_Max.setCreatedBy_id(userDTO_Max.getId());
      CostEntryDTO costEntryDTO_Bill_Max = new CostEntryDTO();
      costEntryDTO_Bill_Max.setExcluded(false);
      costEntryDTO_Bill_Max.setAmount(new BigDecimal(10));
      costEntryDTO_Bill_Max.setName("Schokolade");
      costEntryDTO_Bill_Max.setCategory(categoryDTO);
      billDTO_Max.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Max);
        }
      });

      // Create a bill for user with Id = 2
      BillDTO billDTO_Tina = new BillDTO();
      billDTO_Tina.setId(2);
      billDTO_Tina.setDate(DateTime.now().toDate());
      billDTO_Tina.setCreatedBy_id(userDTO_Tina.getId());
      CostEntryDTO costEntryDTO_Bill_Tina = new CostEntryDTO();
      costEntryDTO_Bill_Tina.setExcluded(false);
      costEntryDTO_Bill_Tina.setAmount(new BigDecimal(10));
      costEntryDTO_Bill_Tina.setName("Obst");
      costEntryDTO_Bill_Tina.setCategory(categoryDTO);
      billDTO_Tina.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Tina);
        }
      });

      // Create a bill for user with Id = 3
      BillDTO billDTO_Luna = new BillDTO();
      billDTO_Luna.setId(3);
      billDTO_Luna.setDate(DateTime.now().toDate());
      billDTO_Luna.setCreatedBy_id(userDTO_Luna.getId());
      CostEntryDTO costEntryDTO_Bill_Luna = new CostEntryDTO();
      costEntryDTO_Bill_Luna.setExcluded(false);
      costEntryDTO_Bill_Luna.setAmount(new BigDecimal(10));
      costEntryDTO_Bill_Luna.setName("Fleisch");
      costEntryDTO_Bill_Luna.setCategory(categoryDTO);
      billDTO_Luna.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Luna);
        }
      });

      List<BillDTO> bills = new ArrayList<>();
      bills.add(billDTO_Max);
      bills.add(billDTO_Tina);
      bills.add(billDTO_Luna);

      when(billFacade.getBillsWithCostEntriesBetweenTimespan(anyObject(), anyObject(), anyInt()))
          .thenReturn(bills);

      costDivisionBean.init();
    } finally {
      context.release();
    }

    Assert.assertEquals(costDivisionBean.getPaymentUsers().size(), 0);
  }

  public void calculatePaymentUsers_shouldCreateTwoPaymentUsersWithAmountEquals4AndReceiverEqualsBillCreator() {
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      CategoryDTO categoryDTO = new CategoryDTO(1, "Lebensmittel", false, false);

      // Create a bill for user with Id = 1
      BillDTO billDTO_Max = new BillDTO();
      billDTO_Max.setId(1);
      billDTO_Max.setDate(DateTime.now().toDate());
      billDTO_Max.setCreatedBy_id(userDTO_Max.getId());
      CostEntryDTO costEntryDTO_Bill_Max = new CostEntryDTO();
      costEntryDTO_Bill_Max.setExcluded(false);
      costEntryDTO_Bill_Max.setAmount(new BigDecimal(12));
      costEntryDTO_Bill_Max.setName("Schokolade");
      costEntryDTO_Bill_Max.setCategory(categoryDTO);
      billDTO_Max.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Max);
        }
      });

      when(billFacade.getBillsWithCostEntriesBetweenTimespan(anyObject(), anyObject(), anyInt()))
          .thenReturn(new ArrayList<BillDTO>() {
            {
              add(billDTO_Max);
            }
          });

      costDivisionBean.init();
    } finally {
      context.release();
    }

    List<PaymentUserDTO> paymentsUsers = costDivisionBean.getPaymentUsers();
    Assert.assertEquals(paymentsUsers.size(), 2);
    Assert.assertEquals(paymentsUsers.get(0).getAmount(),
        new BigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP));
    Assert.assertEquals(paymentsUsers.get(1).getAmount(),
        new BigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP));
    Assert.assertEquals(paymentsUsers.get(0).getReceiver().getId(), userDTO_Max.getId());
    Assert.assertEquals(paymentsUsers.get(1).getReceiver().getId(), userDTO_Max.getId());
  }

  public void calculatePaymentUsers_shouldCreateTwoPaymentUsersWithRoundedAmountsAndReceiverEqualsBillCreator() {
    FacesContext context = ContextMocker.mockFacesContext();
    CostEntryDTO costEntryDTO_Bill_Max;
    try {
      CategoryDTO categoryDTO = new CategoryDTO(1, "Lebensmittel", false, false);

      // Create a bill for user with Id = 1
      BillDTO billDTO_Max = new BillDTO();
      billDTO_Max.setId(1);
      billDTO_Max.setDate(DateTime.now().toDate());
      billDTO_Max.setCreatedBy_id(userDTO_Max.getId());
      costEntryDTO_Bill_Max = new CostEntryDTO();
      costEntryDTO_Bill_Max.setExcluded(false);
      costEntryDTO_Bill_Max.setAmount(new BigDecimal(10));
      costEntryDTO_Bill_Max.setName("Schokolade");
      costEntryDTO_Bill_Max.setCategory(categoryDTO);
      billDTO_Max.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Max);
        }
      });

      when(billFacade.getBillsWithCostEntriesBetweenTimespan(anyObject(), anyObject(), anyInt()))
          .thenReturn(new ArrayList<BillDTO>() {
            {
              add(billDTO_Max);
            }
          });

      costDivisionBean.init();
    } finally {
      context.release();
    }

    List<PaymentUserDTO> paymentsUsers = costDivisionBean.getPaymentUsers();
    Assert.assertEquals(paymentsUsers.size(), 2);
    Assert.assertEquals(paymentsUsers.get(0).getAmount(),
        costEntryDTO_Bill_Max.getAmount().divide(new BigDecimal(3), RoundingMode.UP));
    Assert.assertEquals(paymentsUsers.get(1).getAmount(),
        costEntryDTO_Bill_Max.getAmount().divide(new BigDecimal(3), RoundingMode.UP));
    Assert.assertEquals(paymentsUsers.get(0).getReceiver().getId(), userDTO_Max.getId());
    Assert.assertEquals(paymentsUsers.get(1).getReceiver().getId(), userDTO_Max.getId());
  }

  public void calculatePaymentUsers_shouldOnlyCreateOnePaymentUsersDueToCustomCategory() {
    FacesContext context = ContextMocker.mockFacesContext();
    try {
      CategoryDTO categoryDTO = new CategoryDTO(1, "Lebensmittel", false, false);

      UserGroupCategoryDTO userGroupCategoryDTO_Max = new UserGroupCategoryDTO();
      userGroupCategoryDTO_Max.setPercent(new BigDecimal(0.5));
      userGroupCategoryDTO_Max.setCategory(categoryDTO);
      userGroupCategoryDTO_Max.setUserGroupDTO(userGroupDTO_Max);

      UserGroupCategoryDTO userGroupCategoryDTO_Tina = new UserGroupCategoryDTO();
      userGroupCategoryDTO_Tina.setPercent(new BigDecimal(0.5));
      userGroupCategoryDTO_Tina.setCategory(categoryDTO);
      userGroupCategoryDTO_Tina.setUserGroupDTO(userGroupDTO_Tina);

      UserGroupCategoryDTO userGroupCategoryDTO_Luna = new UserGroupCategoryDTO();
      userGroupCategoryDTO_Luna.setPercent(new BigDecimal(0));
      userGroupCategoryDTO_Luna.setCategory(categoryDTO);
      userGroupCategoryDTO_Luna.setUserGroupDTO(userGroupDTO_Luna);

      List<UserGroupCategoryDTO> userGroupCategoryDTOList = new ArrayList<UserGroupCategoryDTO>() {
        {
          add(userGroupCategoryDTO_Max);
          add(userGroupCategoryDTO_Luna);
          add(userGroupCategoryDTO_Tina);
        }
      };

      categoryDTO.setCustomDivision(true);

      when(userGroupCategoryFacade.findByCategory(categoryDTO.getId())).thenReturn(
          userGroupCategoryDTOList);

      // Create a bill for user with Id = 1
      BillDTO billDTO_Max = new BillDTO();
      billDTO_Max.setId(1);
      billDTO_Max.setDate(DateTime.now().toDate());
      billDTO_Max.setCreatedBy_id(userDTO_Max.getId());
      CostEntryDTO costEntryDTO_Bill_Max = new CostEntryDTO();
      costEntryDTO_Bill_Max.setExcluded(false);
      costEntryDTO_Bill_Max.setAmount(new BigDecimal(10));
      costEntryDTO_Bill_Max.setName("Schokolade");
      costEntryDTO_Bill_Max.setCategory(categoryDTO);
      billDTO_Max.setCostEntries(new ArrayList<CostEntryDTO>() {
        {
          add(costEntryDTO_Bill_Max);
        }
      });

      when(billFacade.getBillsWithCostEntriesBetweenTimespan(anyObject(), anyObject(), anyInt()))
          .thenReturn(new ArrayList<BillDTO>() {
            {
              add(billDTO_Max);
            }
          });

      costDivisionBean.init();
    } finally {
      context.release();
    }

    List<PaymentUserDTO> paymentsUsers = costDivisionBean.getPaymentUsers();
    Assert.assertEquals(paymentsUsers.size(), 1);
    Assert.assertEquals(paymentsUsers.get(0).getAmount(),
        new BigDecimal(5).setScale(2, RoundingMode.UP));
    Assert.assertEquals(paymentsUsers.get(0).getReceiver().getId(), userDTO_Max.getId());
  }
}
