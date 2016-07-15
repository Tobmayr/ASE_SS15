package com.smartwg.core.internal.repositories.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.CostEntry;
import com.smartwg.core.internal.repositories.CostEntryRepository;
import com.smartwg.core.util.Constants;

/**
 * @author Tobias Ortmayr (to) , Oezde Simsek (os)
 */
@Named
public class CostEntryRepositoryImpl extends GenericRepositoryImpl<CostEntry> implements
    CostEntryRepository {

  @PersistenceContext
  EntityManager entityManager;


  @SuppressWarnings("unchecked")
  @Override
  public List<CostEntryDTO> getCostEntries(Integer bill_id) {
    Query query = entityManager.createNamedQuery(Constants.QUERY_COSTENTRIES_BY_ID);
    return query.setParameter("id", bill_id).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CostEntryDTO> findCostEntryByParameters(Integer group_id, Integer createdBy_id,
      Date start, Date end, Integer category_id, Integer shop_id) {
    List<CostEntryDTO> list = new ArrayList<CostEntryDTO>();
    Query query;
    if (group_id == null)
      return list;
    String queryString = "SELECT c FROM CostEntry c where c.bill.group.id=" + group_id;
    if (createdBy_id != -1) {
      queryString += " AND c.bill.createdBy.id=" + createdBy_id;
    }
    if (shop_id != -1) {
      queryString += " AND c.bill.shop.id=" + shop_id;
    }
    if (category_id != -1) {
      queryString += " AND c.category.id=" + category_id;
    }
    if (start != null && end != null) {
      queryString += " AND ( c.bill.date BETWEEN :start AND :end) ORDER BY c.bill.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
      query.setParameter("end", end, TemporalType.DATE);
    } else if (start != null && end == null) {
      queryString += " AND ( c.bill.date >= :start) ORDER BY c.bill.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
    } else if (start == null && end != null) {
      queryString += " AND ( c.bill.date <= :end) ORDER BY c.bill.date DESC";
      query = entityManager.createQuery(queryString);
      query.setParameter("end", end, TemporalType.DATE);
    } else {
      queryString += " ORDER BY c.bill.date DESC";
      query = entityManager.createQuery(queryString);
    }

    for (CostEntry a : (List<CostEntry>) query.getResultList()) {
      CostEntryDTO ce = new CostEntryDTO(a);
      ce.setDate(a.getBill().getDate());
      list.add(ce);
    }
    return list;
  }

  @Override
  public List<CostEntryDTO> getPrivateCostEntries(Integer bill_id) {
    List<CostEntryDTO> temp = getCostEntries(bill_id);
    List<CostEntryDTO> returnList = new ArrayList<CostEntryDTO>();
    for (CostEntryDTO c : temp) {
      if (c.isExcluded()) {
        returnList.add(c);
      }
    }
    return returnList;
  }

}
