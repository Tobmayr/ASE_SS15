package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * Group repository
 *
 * @author Kamil Sierzant (ks)
 */
@Named
public class GroupRepositoryImpl extends GenericRepositoryImpl<Group> implements GroupRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepositoryImpl.class);

  @Override
  @SuppressWarnings("unchecked")
  public List<GroupDTO> findGroups() {
    final Query query = em.createNamedQuery(Constants.QUERY_ALL_GROUPS);
    LOGGER.info("find all groups");
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<GroupDTO> findGroupsByUserID(int id) {
    final Query query = em.createNamedQuery(Constants.QUERY_ALL_GROUPS_BY_USER);
    query.setParameter("userid", id);
    LOGGER.info("find all groups by userid: " + id);
    return query.getResultList();
  }

  @Override
  public boolean isAdmin(int groupId, int userId) {
    final String queryString =
        String.format(
            "SELECT ug FROM UserGroup ug WHERE ug.group.id=%s AND ug.user.id =%s AND role='%s'",
            groupId, userId, Role.ADMIN);
    final Query query = em.createQuery(queryString);
    return !query.getResultList().isEmpty();

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<GroupDTO> searchGroups(String searchparameter) {
    searchparameter = "%" + searchparameter + "%";
    final String queryString =
        "SELECT g FROM Group g WHERE g.name like :param OR g.country.name like :param OR g.city like :param OR g.zip like :param OR g.street like :param OR g.street2 like :param";

    final Query query = em.createQuery(queryString);
    query.setParameter("param", searchparameter);

    return query.getResultList();
  }
}
