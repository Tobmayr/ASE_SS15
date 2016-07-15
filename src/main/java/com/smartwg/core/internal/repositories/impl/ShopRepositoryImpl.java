package com.smartwg.core.internal.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.entities.Shop;
import com.smartwg.core.internal.repositories.ShopRepository;


/**
 * @author Kamil Sierzant (ks)
 */
@Named
public class ShopRepositoryImpl extends GenericRepositoryImpl<Shop> implements ShopRepository {

  @PersistenceContext
  private EntityManager em;

  @SuppressWarnings("unchecked")
  @Override
  public List<ShopDTO> findByGroup(Integer group, Integer country) {
    if (group == null) {
      return new ArrayList<ShopDTO>();
    }
    String queryString =
        "SELECT  new com.smartwg.core.internal.domain.dtos.ShopDTO(s) FROM Shop s WHERE s.group.id="
            + group;
    if (country != null) {
      queryString += " OR s.country.id=" + country;
    }

    return em.createQuery(queryString).getResultList();
  }

}
