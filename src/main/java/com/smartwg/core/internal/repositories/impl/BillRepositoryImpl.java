package com.smartwg.core.internal.repositories.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.CostEntry;
import com.smartwg.core.internal.repositories.BillRepository;
import com.smartwg.core.util.Constants;

/**
 * @author Tobias Ortmayr (to) , Oezde Simsek (os)
 */
@Named
public class BillRepositoryImpl extends GenericRepositoryImpl<Bill> implements BillRepository {

  @PersistenceContext
  private EntityManager entityManager;


  @SuppressWarnings("unchecked")
  @Override
  public List<BillDTO> getBillsBetweenTimespan(Date start, Date end, Integer group_id) {
    Query query = entityManager.createNamedQuery(Constants.QUERY_COLLECTIVE_BILLS_TIMESPAN);
    query.setParameter("start", start);
    query.setParameter("end", end);
    return query.setParameter("group", group_id).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BillDTO> getBillsWithCostEntriesBetweenTimespan(Date start, Date end, Integer group_id) {
    Query query =
        entityManager.createNamedQuery(Constants.QUERY_COLLECTIVE_BILLS_WITH_COST_ENTRIES_TIMESPAN);
    query.setParameter("start", start);
    query.setParameter("end", end);
    return query.setParameter("group", group_id).getResultList();
  }


  @SuppressWarnings("unchecked")
  @Override
  public List<BillDTO> getPrivateBillsBetweenTimespan(Date start, Date end, Integer group_id,
      Integer user_id) {

    String queryString =
        String.format("SELECT b from Bill b  WHERE b.group.id=%s AND b.createdBy.id=%s "
            + " AND b.date BETWEEN :start AND :end", group_id, user_id);

    Query query = entityManager.createQuery(queryString);
    query.setParameter("start", start).setParameter("end", end);
    List<Bill> result = query.getResultList();
    List<BillDTO> temp = new ArrayList<BillDTO>();
    for (Bill b : result) {
      Integer paymentId = null;
      if (b.getPayment() != null) {
        paymentId = b.getPayment().getId();
      }
      if (b.isPrivateBill()) {

        temp.add(new BillDTO(b.getId(), b.getName(), b.getDate(), b.getCreatedBy().getId(), b
            .getCreatedBy().getUserName(), b.getTotal(), b.isPrivateBill(),
            b.getCurrency().getId(), b.getCurrency().getIsoCode(), b.getGroup().getId(), b
                .getShop(), paymentId));
      } else {
        BillDTO tempBill = null;
        List<CostEntryDTO> tempCosts = new ArrayList<CostEntryDTO>();
        for (CostEntry c : b.getCostEntries()) {


          if (c.isExcluded()) {
            if (tempBill == null) {
              tempBill =
                  new BillDTO(b.getId(), b.getName(), b.getDate(), b.getCreatedBy().getId(), b
                      .getCreatedBy().getUserName(), b.getTotal(), b.isPrivateBill(), b
                      .getCurrency().getId(), b.getCurrency().getIsoCode(), b.getGroup().getId(),
                      b.getShop(), paymentId);
            }
            tempCosts.add(new CostEntryDTO(c));

          }
        }
        if (tempBill != null) {
          tempBill.setCostEntries(tempCosts);
          temp.add(tempBill);
        }
      }

    }
    return temp;
  }


  @SuppressWarnings("unchecked")
  @Override
  public BillDTO findBillById(Integer id) {
    Query query = entityManager.createNamedQuery(Constants.QUERY_FIND_BILL_BY_ID);
    List<BillDTO> temp = query.setParameter("id", id).getResultList();
    if (!temp.isEmpty())
      return temp.get(0);
    else
      return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BillDTO> findByParameters(Integer group_id, Integer createdBy_id, Date start,
      Date end, Integer shop_id) {
    List<BillDTO> list = new ArrayList<BillDTO>();
    Query query;
    if (group_id == null)
      return list;
    String queryString = "SELECT b FROM Bill b where b.group.id=" + group_id;
    if (createdBy_id != -1) {
      queryString += " AND b.createdBy.id=" + createdBy_id;
    }
    if (shop_id != -1) {
      queryString += " AND b.shop.id=" + shop_id;
    }
    if (start != null && end != null) {
      queryString += " AND ( b.date BETWEEN :start AND :end) ORDER BY b.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
      query.setParameter("end", end, TemporalType.DATE);
    } else if (start != null && end == null) {
      queryString += " AND ( b.date >= :start) ORDER BY b.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
    } else if (start == null && end != null) {
      queryString += " AND ( b.date <= :end) ORDER BY b.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("end", end, TemporalType.DATE);
    } else {
      queryString += " ORDER BY b.date DESC";
      query = entityManager.createQuery(queryString);
    }

    for (Bill a : (List<Bill>) query.getResultList()) {
      list.add(new BillDTO(a));
    }
    return list;
  }

}
