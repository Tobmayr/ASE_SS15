package com.smartwg.core.facades;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;


/**
 * Central access point for service logic which is associated with absences. In case of implementing
 * a REST-API in the future its methods will be bounded on the facade-interfaces
 * 
 * Anna Sadriu (as)
 *
 */
@Transactional
public interface AbsenceFacade {

  /**
   * Retrieves all absences which are associated with a certain user in a certain group from the
   * database
   * 
   * @param userId User whose absences should be retrieved
   * @param groupId Group of the user
   * @return Matching absences as AbsenceDTO or an empty List
   */
  List<AbsenceDTO> getUserAbsences(Integer userId, Integer groupId);

  /**
   * Retrieves all absences which are associated with a certain group from the database
   * 
   * @param groupId Group of which the absences should be retrieved
   * @return Matching absences as AbsenceDTO or an empty List
   */
  List<AbsenceDTO> getGroupAbsences(Integer groupId);

  /**
   * Persists a new bill or updates an existing bill in the database
   * 
   * @param AbsenceDTO Absence object which should be stored
   * 
   */
  void saveAbsence(AbsenceDTO newAbsence);

  /**
   * Deletes an absence with a certain id of the database. If the value is null the database will
   * remain unchanged
   * 
   * @param absenceDto Absence which should be deleted
   */
  void deleteAbsence(Integer absenceId);

  /**
   * Searches for a certain absence in the database using its id.
   * 
   * @param id Id of the wanted absence
   * @return found Absence as AbsenceDTO, null if the passed id doesnt exist
   */
  AbsenceDTO getAbsenceById(Integer integer);

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
