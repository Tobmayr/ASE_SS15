package com.smartwg.core.internal.services.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.entities.Absence;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;
import com.smartwg.core.internal.repositories.AbsenceRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.services.AbsenceService;
import com.smartwg.core.internal.services.EntityConverter;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class AbsenceServiceImpl implements AbsenceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbsenceServiceImpl.class);

  @Inject
  private AbsenceRepository absenceRepository;
  @Inject
  private EntityConverter absenceFactory;
  @Inject
  private UserGroupRepository userGroupRepository;

  @SuppressWarnings("unchecked")
  @Override
  public List<AbsenceDTO> getUserAbsences(final Integer userId, final Integer groupId) {
    final List<AbsenceDTO> result = absenceRepository.findUserAbsences(userId, groupId);
    LOGGER.info(String.format("%d absence(s) has been found for user with the id %d",
        result.size(), userId));
    return result.isEmpty() ? Collections.EMPTY_LIST : result;
  }

  @Override
  public List<AbsenceDTO> getGroupAbsences(final Integer groupId) {
    final List<AbsenceDTO> result = absenceRepository.findGroupAbsences(groupId);
    LOGGER.info("%d absence(s) has been found for group with the id %d", result.size(), groupId);

    return result.isEmpty() ? Collections.EMPTY_LIST : result;
  }

  @Override
  public void saveAbsence(final AbsenceDTO absenceDTO) {
    final Absence absence = absenceFactory.createAbsence(absenceDTO);
    absence.setUserGroup(userGroupRepository.findById(new UserGroupId(absence.getUserGroup()
        .getGroup().getId(), absence.getUserGroup().getUser().getId())));
    absenceRepository.save(absence);
  }

  @Override
  public void updateAbsence(final AbsenceDTO absenceDTO) {
    final Absence absence = absenceRepository.findById(absenceDTO.getId());
    absence.setAwayFrom(absenceDTO.getAwayFrom());
    absence.setAwayTill(absenceDTO.getAwayTill());
    absence.setTemporary(absenceDTO.isTemporary());

    absenceRepository.save(absence);
  }

  @Override
  public void deleteAbsence(final Integer absenceId) {
    final Absence absence = absenceRepository.findById(absenceId);
    if (absence != null) {
      LOGGER.info(String.format("Absence with the id %d has been removed.", absenceId));
      absenceRepository.delete(absence);
    } else {
      LOGGER.error(String.format("Absence with the id %d could not be found.", absenceId));
    }
  }

  @Override
  public AbsenceDTO getAbsenceById(final Integer absenceId) {
    return absenceRepository.findAbsenceById(absenceId);
  }

  @Override
  public List<AbsenceDTO> findGroupAbsencesBetweenTimespan(Integer groupId, Date begin, Date end) {
    return absenceRepository.findGroupAbsencesBetweenTimespan(groupId, begin, end);
  }
}
