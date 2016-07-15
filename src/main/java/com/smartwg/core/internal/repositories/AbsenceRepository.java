package com.smartwg.core.internal.repositories;

import java.util.Date;
import java.util.List;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.entities.Absence;

/**
 * This repository provides methods for CRUD-Operations for Absence-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * Absence-objects the methods provided by this interface directly return AbsenceDTOs to avoid later
 * typecasting or converting.
 *
 * @author Kamil Sierzant (ks),Anna Sadriu (as)
 */
public interface AbsenceRepository extends GenericRepository<Absence> {

  /**
   * Retrieves all absences which are associated with a certain user in a certain group from the
   * database
   * 
   * @param userId User whose absences should be retrieved
   * @param groupId Group of the user
   * @return List of matching absences as AbsenceDTO or an empty List
   */
  List<AbsenceDTO> findUserAbsences(Integer userId, Integer groupId);

  /**
   * Retrieves all absences which are associated with a certain group from the database
   * 
   * @param groupId Group of which the absences should be retrieved
   * @return List of matching absences as AbsenceDTO or an empty List
   */
  List<AbsenceDTO> findGroupAbsences(Integer groupId);

  /**
   * Searches for a certain absence in the database using its id.
   * 
   * @param absenceId Id of the wanted absence
   * @return found Absence as AbsenceDTO, null if the passed id doesnt exist
   */
  AbsenceDTO findAbsenceById(Integer absenceId);

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
