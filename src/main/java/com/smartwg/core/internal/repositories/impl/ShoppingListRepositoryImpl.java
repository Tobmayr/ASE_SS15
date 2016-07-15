package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.entities.ShoppingList;
import com.smartwg.core.internal.repositories.ShoppingListRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamil Sierzant (ks)
 */
@Named
public class ShoppingListRepositoryImpl extends GenericRepositoryImpl<ShoppingList> implements
    ShoppingListRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListRepositoryImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @SuppressWarnings("unchecked")
  public List<ShoppingListDTO> findUserShoppingLists(final Integer userId, final Integer groupId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_SHOPPINGLISTS_USERID).setParameter(
            "userId", userId).setParameter("group",groupId);
    LOGGER.info(String.format("find shopping lists for user with id %d", userId));
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ShoppingListDTO> findGroupShoppingLists(final Integer groupId,
      final List<ShoppingListState> listStates) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_SHOPPINGLISTS_GROUP_STATE)
            .setParameter("groupId", groupId).setParameter("listState", listStates);
    LOGGER.info(String.format("find shopping lists for group with id %d in states (%s)", groupId,
        listStates));
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ShoppingListDTO> findShoppingListsAssignedToUser(final Integer userId,
      final Integer groupId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_SHOPPINGLISTS_ASSIGNED_TO_USER)
            .setParameter("userId", userId).setParameter("group",groupId);
    LOGGER.info(String.format("find shopping lists assigned to user with id %d", userId));
    return query.getResultList();
  }

  @Override
  public ShoppingListDTO findShoppingListById(final Integer listId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_SHOPPINGLIST_ID).setParameter("listId",
            listId);
    return (ShoppingListDTO) query.getSingleResult();
  }
}
