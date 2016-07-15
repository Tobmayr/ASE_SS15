package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.services.AbsenceService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class AbsenceFacadeImpl implements AbsenceFacade {

  @Inject
  private AbsenceService absenceService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.AbsenceFacade#getUserAbsences(java.lang.Integer,
   *      java.lang.Integer)
   */

  @Override
  public List<AbsenceDTO> getUserAbsences(final Integer userId, final Integer groupId) {
    return absenceService.getUserAbsences(userId, groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.AbsenceFacade#getGroupAbsences(java.lang.Integer)
   */

  @Override
  public List<AbsenceDTO> getGroupAbsences(final Integer groupId) {
    return absenceService.getGroupAbsences(groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.AbsenceFacade#saveAbsence(com.smartwg.core.internal.domain.dtos.AbsenceDTO)
   */

  @Override
  public void saveAbsence(final AbsenceDTO absence) {
    if (absence.getId() == null) {
      absenceService.saveAbsence(absence);
    } else {
      absenceService.updateAbsence(absence);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.AbsenceFacade#deleteAbsence(java.lang.Integer)
   */

  @Override
  public void deleteAbsence(final Integer absenceId) {
    absenceService.deleteAbsence(absenceId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.AbsenceFacade#getAbsenceById(java.lang.Integer)
   */

  @Override
  public AbsenceDTO getAbsenceById(final Integer absenceId) {
    return absenceService.getAbsenceById(absenceId);
  }
  @Override
  public List<AbsenceDTO> findGroupAbsencesBetweenTimespan(Integer groupId, Date begin, Date end){
    return absenceService.findGroupAbsencesBetweenTimespan(groupId, begin, end);
  }
}
