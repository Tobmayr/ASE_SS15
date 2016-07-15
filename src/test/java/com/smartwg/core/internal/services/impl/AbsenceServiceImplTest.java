package com.smartwg.core.internal.services.impl;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.entities.Absence;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.AbsenceRepository;
import com.smartwg.core.internal.services.AbsenceService;
import com.smartwg.core.internal.services.EntityConverter;

/**
 * @author Kamil Sierzant (ks)
 */
@Test
public class AbsenceServiceImplTest {

  private static final Integer USER_ID = 3;
  private static final Integer GROUP_ID = 4;
  private static final Integer ABSENCE_ID = 1;
  private static final Date START = new DateTime(DateTime.now().getYear(), DateTime.now().getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
  private static final Date END = new DateTime(DateTime.now().getYear(), DateTime.now().getMonthOfYear(), DateTime.now().getDayOfMonth(), 0, 0, 0, 0).toDate();;
  
  @InjectMocks
  private AbsenceService service;

  @Mock
  private AbsenceRepository absenceRepository;
  @Mock
  private EntityConverter absenceFactory;

  @Mock
  private AbsenceDTO absenceDTO;
  @Mock
  private AbsenceDTO secondAbsenceDTO;
  @Mock
  private Absence absence;

  @BeforeMethod
  public void setUp() {
    service = new AbsenceServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getUserAbsencesSucceed() {
    when(absenceRepository.findUserAbsences(USER_ID, GROUP_ID)).thenReturn(
        asList(absenceDTO, secondAbsenceDTO));

    final List<AbsenceDTO> result = service.getUserAbsences(USER_ID, GROUP_ID);

    assertEquals(result, asList(absenceDTO, secondAbsenceDTO));
  }

  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void getUserAbsencesWhenNothingFound() {
    when(absenceRepository.findUserAbsences(USER_ID, GROUP_ID)).thenReturn(Collections.emptyList());

    final List<AbsenceDTO> result = service.getUserAbsences(USER_ID, GROUP_ID);

    assertTrue(result.isEmpty());
    result.add(absenceDTO);
  }

  public void getGroupAbsencesSucceed() {
    when(absenceRepository.findGroupAbsences(eq(GROUP_ID))).thenReturn(
        asList(absenceDTO, secondAbsenceDTO));

    final List<AbsenceDTO> result = service.getGroupAbsences(GROUP_ID);

    assertEquals(result, asList(absenceDTO, secondAbsenceDTO));

  }

  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void getGroupAbsencesWhenNothingFound() {
    when(absenceRepository.findGroupAbsences(eq(GROUP_ID))).thenReturn(Collections.emptyList());

    final List<AbsenceDTO> result = service.getGroupAbsences(GROUP_ID);

    assertTrue(result.isEmpty());
    result.add(absenceDTO);
  }


  public void updateAbsenceSucceed() {
    final Date now = DateTime.now().toDate();
    final Date tomorrow = DateTime.now().plusDays(1).toDate();

    when(absenceDTO.getId()).thenReturn(ABSENCE_ID);
    when(absenceDTO.getAwayFrom()).thenReturn(now);
    when(absenceDTO.getAwayTill()).thenReturn(tomorrow);
    when(absenceDTO.isTemporary()).thenReturn(true);

    when(absenceRepository.findById(eq(ABSENCE_ID))).thenReturn(absence);

    service.updateAbsence(absenceDTO);

    verify(absence, times(1)).setAwayFrom(eq(now));
    verify(absence, times(1)).setAwayTill(eq(tomorrow));
    verify(absence, times(1)).setTemporary(eq(true));
    verify(absenceRepository, times(1)).save(eq(absence));
  }

  public void deleteAbsenceSucceed() {
    when(absenceRepository.findById(eq(ABSENCE_ID))).thenReturn(absence);

    service.deleteAbsence(ABSENCE_ID);

    verify(absenceRepository, times(1)).delete(eq(absence));
  }

  public void deleteAbsenceWhenAbsenceNotFound() {
    when(absenceRepository.findById(eq(ABSENCE_ID))).thenReturn(null);

    service.deleteAbsence(ABSENCE_ID);

    verify(absenceRepository, never()).delete(any(Absence.class));
  }

  public void getAbsenceByIdSucceed() {
    when(absenceRepository.findAbsenceById(eq(ABSENCE_ID))).thenReturn(absenceDTO);

    final AbsenceDTO result = service.getAbsenceById(ABSENCE_ID);

    assertEquals(result, absenceDTO);
  }
  
  public void findGroupAbsencesBetweenTimespan() {
    when(absenceRepository.findGroupAbsencesBetweenTimespan(USER_ID, START, END)).thenReturn(
        asList(absenceDTO, secondAbsenceDTO));

    final List<AbsenceDTO> result = service.findGroupAbsencesBetweenTimespan(USER_ID, START, END);

    assertEquals(result, asList(absenceDTO, secondAbsenceDTO));
  }
}
