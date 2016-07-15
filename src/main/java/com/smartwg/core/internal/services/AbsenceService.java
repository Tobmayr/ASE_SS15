package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;

import java.util.Date;
import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with absence. e.g.
 * persisting and updating absences in the database etc. The main purpose of these methods is to
 * wrap the functionality of the underlying AbsenceRepository and convert the entities to dtos for
 * use in higher-tiered layers
 * 
 * @author Anna Sadriu (as)
 */
public interface AbsenceService {

  /**
   * Returns the user absences for a given user and a given group
   *
   * @param userId {@link Integer} of the user
   * @param groupId {@link Integer} of the group
   * @return a {@link List} of {@link AbsenceDTO}s containing required information
   */
  List<AbsenceDTO> getUserAbsences(Integer userId, Integer groupId);

  /**
   * Returns the user absences for a given group
   *
   * @param groupId {@link Integer} of the group
   * @return a {@link List} of {@link AbsenceDTO}s containing required information
   */
  List<AbsenceDTO> getGroupAbsences(Integer groupId);

  /**
   * Creates a new absence based on information provided in dto
   *
   * @param absenceDTO {@link AbsenceDTO} with information about the user absence
   */
  void saveAbsence(AbsenceDTO absenceDTO);

  /**
   * Updates the given absence with information provided in dto
   *
   * @param absenceDTO {@link AbsenceDTO} with information to be updated
   */
  void updateAbsence(AbsenceDTO absenceDTO);

  /**
   * Deletes the absence with given id
   *
   * @param absenceId {@link Integer} of the absence to be deleted
   */
  void deleteAbsence(Integer absenceId);

  /**
   * Returns the absence dto for an absence with given id
   *
   * @param absenceId {@link Integer} of the absence
   * @return {@link AbsenceDTO}
   */
  AbsenceDTO getAbsenceById(Integer absenceId);

  /**
   * Retrieves all absences which are associated with a certain group and are in the defined
   * timespan from the database
   * 
   * @param groupId Group of which the absences should be retrieved
   * @param begin start of the timespan
   * @param end end of the timespan
   * @return Matching absences as AbsenceDTO or an empty List
   */
  List<AbsenceDTO> findGroupAbsencesBetweenTimespan(Integer groupId, Date begin, Date end);
}
