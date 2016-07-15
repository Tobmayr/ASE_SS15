package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.entities.Absence;
import com.smartwg.core.internal.repositories.AbsenceRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamil Sierzant (ks),Anna Sadriu (as)
 */
@Named
public class AbsenceRepositoryImpl extends GenericRepositoryImpl<Absence> implements
    AbsenceRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbsenceRepositoryImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @SuppressWarnings("unchecked")
  public List<AbsenceDTO> findUserAbsences(final Integer userId, final Integer groupId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_ABSENCES_USERID)
            .setParameter("userId", userId).setParameter("group", groupId);
    LOGGER.info(String.format("find absence(s) for user with id %d", userId));
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AbsenceDTO> findGroupAbsences(final Integer groupId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_ABSENCES_GROUP).setParameter("groupId",
            groupId);
    LOGGER.info(String.format("find absence(s) for group with id %d", groupId));
    return query.getResultList();
  }
  @Override
  public List<AbsenceDTO>findGroupAbsencesBetweenTimespan(final Integer groupId, final Date begin, final Date end ){
    return null;
  }

  @Override
  public AbsenceDTO findAbsenceById(Integer absenceId) {
    final Query query =
        entityManager.createNamedQuery(Constants.QUERY_FIND_ABSENCE_ID).setParameter("absenceId",
            absenceId);
    return (AbsenceDTO) query.getSingleResult();
  }
}
