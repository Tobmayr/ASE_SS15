package com.smartwg.core.facades.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.PaymentFacade;
import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.repositories.PaymentRepository;
import com.smartwg.core.internal.services.PaymentService;

public class PaymentFacadeImplTest {

  @InjectMocks
  private PaymentFacade facade;

  @Mock
  private PaymentDTO paymentDTO;

  @Mock
  private PaymentUserDTO paymentUserDTO;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private UserDTO sender;

  @Mock
  private PaymentService paymentService;

  @BeforeMethod
  public void beforeMethod() {
    facade = new PaymentFacadeImpl();
    MockitoAnnotations.initMocks(this);
    paymentDTO = new PaymentDTO(1, DateTime.now().toDate(), null);
    sender = new UserDTO();
    sender.setId(1);
    UserDTO receiver = new UserDTO();
    receiver.setId(2);
    paymentUserDTO = new PaymentUserDTO(new BigDecimal(10), false, sender, receiver, 1);
  }

  @Test
  public void createOrUpdatePayment_SucceedUpdate() {
    when(paymentService.editPayment(paymentDTO)).thenReturn(paymentDTO.getId());
    Assert.assertEquals(facade.createOrUpdatePayment(paymentDTO), paymentDTO.getId());
    verify(paymentService).editPayment(paymentDTO);
  }

  @Test
  public void createOrUpdatePayment_SucceedCreate() {
    Integer expectedPaymentId = paymentDTO.getId();
    when(paymentService.createPayment(paymentDTO)).thenReturn(expectedPaymentId);
    paymentDTO.setId(null);
    Assert.assertEquals(facade.createOrUpdatePayment(paymentDTO), expectedPaymentId);
    verify(paymentService).createPayment(paymentDTO);
  }

  @Test
  public void createOrUpdatePayment_Fail() {
    Assert.assertEquals(facade.createOrUpdatePayment(null), new Integer(-1));
    verify(paymentService, Mockito.times(0)).editPayment(paymentDTO);
    verify(paymentService, Mockito.times(0)).createPayment(paymentDTO);
  }

  @Test
  public void confirmUserPayment_Succeed() {
    facade.confirmUserPayment(paymentUserDTO);
    verify(paymentService).confirmUserPayment(paymentUserDTO);
  }

  @Test
  public void confirmUserPayment_Fail() {
    facade.confirmUserPayment(null);
    verify(paymentService, Mockito.times(0)).confirmUserPayment(paymentUserDTO);
  }

  @Test
  public void findById_Succeed() {
    when(paymentService.findById(paymentDTO.getId())).thenReturn(paymentDTO);
    Assert.assertEquals(facade.findById(paymentDTO.getId()), paymentDTO);
    verify(paymentService).findById(paymentDTO.getId());
  }

  @Test
  public void findUnconfirmedPaymentUsersByUserId_Succeed() {
    List<PaymentUserDTO> expectedResult = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    when(paymentService.findUnconfirmedPaymentUsersByUserId(sender.getId())).thenReturn(
        expectedResult);
    Assert.assertEquals(facade.findUnconfirmedPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentService).findUnconfirmedPaymentUsersByUserId(sender.getId());
  }

  @Test
  public void findPaymentUsersByUserId_Succeed() {
    List<PaymentUserDTO> expectedResult = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    when(paymentService.findPaymentUsersByUserId(sender.getId())).thenReturn(expectedResult);
    Assert.assertEquals(facade.findPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentService).findPaymentUsersByUserId(sender.getId());
  }

  @Test
  public void createOrUpdatePaymentUsers_SucceedEdit() {
    List<PaymentUserDTO> paymentUserDTOList = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    facade.createOrUpdatePaymentUsers(paymentUserDTOList, paymentDTO.getId());
    verify(paymentService).editPaymentUser(paymentUserDTO);

    paymentUserDTOList.clear();
    Assert.assertEquals(paymentUserDTOList.size(), 0);
    facade.createOrUpdatePaymentUsers(paymentUserDTOList, paymentDTO.getId());
    facade.createOrUpdatePaymentUsers(null, paymentDTO.getId());
    verify(paymentService, Mockito.times(2)).deletePaymentUsersByPaymentId(paymentDTO.getId());
  }

  @Test
  public void createOrUpdatePaymentUsers_SucceedCreate() {
    paymentUserDTO.setPaymentId(null);
    List<PaymentUserDTO> paymentUserDTOList = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    facade.createOrUpdatePaymentUsers(paymentUserDTOList, paymentDTO.getId());
    verify(paymentService).createPaymentUser(paymentUserDTO);
  }
}
