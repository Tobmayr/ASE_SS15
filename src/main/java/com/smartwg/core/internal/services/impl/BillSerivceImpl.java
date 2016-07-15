package com.smartwg.core.internal.services.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.repositories.BillRepository;
import com.smartwg.core.internal.repositories.CostEntryRepository;
import com.smartwg.core.internal.services.BillService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserService;

/**
 * @author Tobias Ortmayr (to) , Oezde Simsek (os)
 */
@Named
public class BillSerivceImpl implements BillService {

  @Inject
  private BillRepository billRepository;
  @Inject
  private CostEntryRepository costEntryRepository;
  @Inject
  private EntityConverter factory;
  @Inject
  private UserService userService;
  @Inject
  private EmailService emailService;

  @Override
  public void createBill(BillDTO billDTO) {
    Bill bill = factory.createBill(billDTO);
    if (!billDTO.isPrivateBill() && billDTO.getId() == null) {
      sendEmailNotification(billDTO);
    }
    billRepository.save(bill);
  }

  private void sendEmailNotification(final BillDTO billDTO) {
    final HashSet<String> recipients = new HashSet<>();
    for (String recipient : userService.findUsersEmailsByGroupId(billDTO.getGroupId(),
        NotificationType.BILL)) {
      recipients.add(recipient);
    }
    emailService.createAndSendBillCreatedEmail(billDTO, recipients);
  }

  @Override
  public void editBill(BillDTO billDTO) {
    Bill bill = factory.createBill(billDTO);
    billRepository.merge(bill);
  }

  @Override
  public void deleteBill(BillDTO billDTO) {
    Bill bill = new Bill();
    bill.setId(billDTO.getId());
    billRepository.delete(bill);
  }

  @Override
  public List<BillDTO> findBillsBetweenTimespan(Date start, Date end, Integer group_id) {
    return billRepository.getBillsBetweenTimespan(start, end, group_id);
  }

  @Override
  public List<BillDTO> findBillsWithCostEntriesBetweenTimespan(Date start, Date end,
      Integer group_id) {
    return billRepository.getBillsWithCostEntriesBetweenTimespan(start, end, group_id);
  }

  @Override
  public List<BillDTO> findPrivateBillsBetweenTimespan(Date start, Date end, Integer group_id,
      Integer user_id) {
    return billRepository.getPrivateBillsBetweenTimespan(start, end, group_id, user_id);
  }

  @Override
  public BillDTO findById(Integer id) {
    return billRepository.findBillById(id);
  }

  @Override
  public List<CostEntryDTO> getCostEntries(Integer bill_id) {
    return costEntryRepository.getCostEntries(bill_id);
  }

  @Override
  public List<CostEntryDTO> getPrivateCostEntries(Integer bill_id) {
    return costEntryRepository.getPrivateCostEntries(bill_id);
  }

  @Override
  public List<BillDTO> findByParameters(Integer group_id, Integer createdBy_id, Date start,
      Date end, Integer shop_id) {

    return billRepository.findByParameters(group_id, createdBy_id, start, end, shop_id);
  }

  @Override
  public List<CostEntryDTO> findCostEntryByParameters(Integer group_id, Integer createdBy_id,
      Date start, Date end, Integer category_id, Integer shop_id) {
    return costEntryRepository.findCostEntryByParameters(group_id, createdBy_id, start, end,
        category_id, shop_id);
  }


}
