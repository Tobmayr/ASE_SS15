package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.entities.ListPosition;
import com.smartwg.core.internal.repositories.ListPositionRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class ListRepositoryImpl extends GenericRepositoryImpl<ListPosition> implements
    ListPositionRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListRepositoryImpl.class);

  @Override
  @SuppressWarnings("unchecked")
  public List<ListItemDTO> findShoppingListPositions(final Integer shoppingListId) {
    final Query query =
        em.createNamedQuery(Constants.QUERY_FIND_SHOPPINGLIST_POSITION_ID).setParameter(
            "shoppingListId", shoppingListId);

    LOGGER.info(String.format("find shopping list positions of shopping list with id %d",
        shoppingListId));
    return query.getResultList();
  }
}
