package com.smartwg.core.internal.services.impl;

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

import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.entities.Payment;
import com.smartwg.core.internal.domain.entities.PaymentUser;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.repositories.PaymentRepository;
import com.smartwg.core.internal.repositories.PaymentUserRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.PaymentService;

@Test
public class PaymentServiceImplTest {

  @InjectMocks
  private PaymentService service;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private PaymentUserRepository paymentUserRepository;

  @Mock
  private EntityConverter entityConverter;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PaymentDTO paymentDTO;

  @Mock
  private UserDTO sender;

  @Mock
  private UserDTO receiver;

  @Mock
  private PaymentUserDTO paymentUserDTO;

  @BeforeMethod
  public void setUp() {
    service = new PaymentServiceImpl();
    MockitoAnnotations.initMocks(this);
    paymentDTO = new PaymentDTO(1, DateTime.now().toDate(), null);
    sender = new UserDTO();
    sender.setId(1);
    receiver = new UserDTO();
    receiver.setId(2);
    paymentUserDTO = new PaymentUserDTO(new BigDecimal(10), false, sender, receiver, 1);
  }

  public void deletePaymentUsersByPaymentId_Succeed() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set sender
    final User send = new User();
    send.setId(sender.getId());
    paymentUser.setSender(send);

    // Create and set receiver
    final User receive = new User();
    receive.setId(receiver.getId());
    paymentUser.setReceiver(receive);

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    // Mock Repositories
    when(paymentRepository.findPaymentUsersByPaymentId(paymentDTO.getId())).thenReturn(
        new ArrayList<PaymentUserDTO>() {
          {
            add(paymentUserDTO);
          }
        });
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    when(paymentRepository.findById(paymentDTO.getId())).thenReturn(payment);
    when(userRepository.findById(sender.getId())).thenReturn(send);
    when(userRepository.findById(receiver.getId())).thenReturn(receive);

    service.deletePaymentUsersByPaymentId(paymentDTO.getId());

    verify(paymentUserRepository).delete(paymentUser);
  }

  public void deletePaymentUsersByPaymentId_FailNoPaymentUserFoundForId() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set sender
    final User send = new User();
    send.setId(sender.getId());
    paymentUser.setSender(send);

    // Create and set receiver
    final User receive = new User();
    receive.setId(receiver.getId());
    paymentUser.setReceiver(receive);

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    // Mock Repository, which does not find a PaymentUser for the given payment Id:
    when(paymentRepository.findPaymentUsersByPaymentId(paymentDTO.getId())).thenReturn(
        new ArrayList<>());
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);

    service.deletePaymentUsersByPaymentId(paymentDTO.getId());

    verify(paymentUserRepository, Mockito.times(0)).delete(paymentUser);
  }

  public void editPaymentUser_Succeed() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set sender
    final User send = new User();
    send.setId(sender.getId());
    paymentUser.setSender(send);

    // Create and set receiver
    final User receive = new User();
    receive.setId(receiver.getId());
    paymentUser.setReceiver(receive);

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    // Mock Repositories
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    when(paymentRepository.findById(paymentDTO.getId())).thenReturn(payment);
    when(userRepository.findById(sender.getId())).thenReturn(send);
    when(userRepository.findById(receiver.getId())).thenReturn(receive);

    service.editPaymentUser(paymentUserDTO);

    verify(paymentUserRepository).merge(paymentUser);
  }

  public void editPaymentUser_FailNoPayment() {
    final PaymentUser paymentUser = new PaymentUser();
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.editPaymentUser(paymentUserDTO);
    verify(paymentUserRepository, Mockito.times(0)).merge(paymentUser);
  }

  public void editPaymentUser_FailNoSender() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.editPaymentUser(paymentUserDTO);
    verify(paymentUserRepository, Mockito.times(0)).merge(paymentUser);
  }

  public void editPaymentUser_FailNoReceiver() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    // Create and set sender
    final User send = new User();
    send.setId(sender.getId());
    paymentUser.setSender(send);

    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.editPaymentUser(paymentUserDTO);
    verify(paymentUserRepository, Mockito.times(0)).merge(paymentUser);
  }

  public void createPaymentUser_Succeed() {
    final PaymentUser paymentUser = new PaymentUser();

    // Create and set sender
    final User send = new User();
    send.setId(sender.getId());
    paymentUser.setSender(send);

    // Create and set receiver
    final User receive = new User();
    receive.setId(receiver.getId());
    paymentUser.setReceiver(receive);

    // Create and set Payment
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);

    // Mock Repositories
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    when(paymentRepository.findById(paymentDTO.getId())).thenReturn(payment);
    when(userRepository.findById(sender.getId())).thenReturn(send);
    when(userRepository.findById(receiver.getId())).thenReturn(receive);

    service.createPaymentUser(paymentUserDTO);

    verify(paymentUserRepository).save(paymentUser);
  }

  public void createPaymentUser_Fail() {
    final PaymentUser paymentUser = new PaymentUser();
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.createPaymentUser(paymentUserDTO);
    verify(paymentUserRepository, Mockito.times(0)).save(paymentUser);
  }

  public void editPayment_Succeed() {
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    when(entityConverter.createPayment(paymentDTO)).thenReturn(payment);
    Assert.assertEquals(service.editPayment(paymentDTO), payment.getId());
    verify(paymentRepository).merge(payment);
  }

  public void editPayment_Fail() {
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    when(entityConverter.createPayment(paymentDTO)).thenReturn(null);
    int actual = service.editPayment(paymentDTO);
    Assert.assertEquals(actual, -1);
    Assert.assertNotEquals(actual, payment.getId());
    verify(paymentRepository, Mockito.times(0)).merge(payment);
  }

  public void createPayment_Succeed() {
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    when(entityConverter.createPayment(paymentDTO)).thenReturn(payment);
    when(paymentRepository.save(payment)).thenReturn(payment);
    Assert.assertEquals(service.createPayment(paymentDTO), payment.getId());
    verify(paymentRepository).save(payment);
  }

  public void createPayment_Fail() {
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    when(entityConverter.createPayment(paymentDTO)).thenReturn(payment);
    when(paymentRepository.save(payment)).thenReturn(null);
    int actual = service.createPayment(paymentDTO);
    Assert.assertEquals(actual, -1);
    Assert.assertNotEquals(actual, payment.getId());
    verify(paymentRepository).save(payment);
  }

  public void findPaymentUsersByUserId_Succeed() {
    List<PaymentUserDTO> expectedResult = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    when(paymentRepository.findPaymentUsersByUserId(sender.getId())).thenReturn(expectedResult);
    Assert.assertEquals(service.findPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentRepository).findPaymentUsersByUserId(sender.getId());
  }

  public void findPaymentUsersByUserId_Fail() {
    List<PaymentUserDTO> expectedResult = new ArrayList<>();
    when(paymentRepository.findPaymentUsersByUserId(sender.getId())).thenReturn(expectedResult);
    Assert.assertEquals(service.findPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentRepository).findPaymentUsersByUserId(sender.getId());
  }

  public void findUnconfirmedPaymentUsersByUserId_Succeed() {
    List<PaymentUserDTO> expectedResult = new ArrayList<PaymentUserDTO>() {
      {
        add(paymentUserDTO);
      }
    };
    when(paymentRepository.findUnconfirmedPaymentUsersByUserId(sender.getId())).thenReturn(
        expectedResult);
    Assert
        .assertEquals(service.findUnconfirmedPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentRepository).findUnconfirmedPaymentUsersByUserId(sender.getId());
  }

  public void findUnconfirmedPaymentUsersByUserId_Fail() {
    List<PaymentUserDTO> expectedResult = new ArrayList<>();
    when(paymentRepository.findUnconfirmedPaymentUsersByUserId(sender.getId())).thenReturn(
        expectedResult);
    Assert
        .assertEquals(service.findUnconfirmedPaymentUsersByUserId(sender.getId()), expectedResult);
    verify(paymentRepository).findUnconfirmedPaymentUsersByUserId(sender.getId());
  }

  public void findById_Succeed() {
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    when(paymentRepository.findById(1)).thenReturn(payment);
    final PaymentDTO result = service.findById(1);
    Assert.assertNotNull(result);
    Assert.assertEquals(result.getId(), paymentDTO.getId());
  }

  public void findById_Fails() {
    when(paymentRepository.findById(0)).thenReturn(null);
    final PaymentDTO result = service.findById(0);
    Assert.assertEquals(result, null);
  }

  public void confirmUserPayment_Succeed() {
    Assert.assertFalse(paymentUserDTO.isConfirmed());
    final PaymentUser paymentUser = new PaymentUser();
    paymentUser.setConfirmed(paymentUserDTO.isConfirmed());
    final User send = new User();
    send.setId(sender.getId());
    final User receive = new User();
    receive.setId(receiver.getId());
    paymentUser.setSender(send);
    paymentUser.setReceiver(receive);
    final Payment payment = new Payment();
    payment.setId(paymentDTO.getId());
    paymentUser.setPayment(payment);
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.confirmUserPayment(paymentUserDTO);
    verify(paymentUserRepository).merge(paymentUser);
    Assert.assertTrue(paymentUser.isConfirmed());
  }

  public void confirmUserPayment_Fail() {
    Assert.assertFalse(paymentUserDTO.isConfirmed());
    final PaymentUser paymentUser = new PaymentUser();
    paymentUser.setConfirmed(paymentUserDTO.isConfirmed());
    when(entityConverter.createPaymentUser(paymentUserDTO)).thenReturn(paymentUser);
    service.confirmUserPayment(paymentUserDTO);
    Assert.assertFalse(paymentUser.isConfirmed());
  }
}
