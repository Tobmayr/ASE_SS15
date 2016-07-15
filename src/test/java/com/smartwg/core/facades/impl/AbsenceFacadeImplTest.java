package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.services.AbsenceService;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class AbsenceFacadeImplTest {

  private static final Integer USER_ID = 3;
  private static final Integer GROUP_ID = 2;
  private static final Date START = new DateTime(DateTime.now().getYear(), DateTime.now().getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
  private static final Date END = new DateTime(DateTime.now().getYear(), DateTime.now().getMonthOfYear(), DateTime.now().getDayOfMonth(), 0, 0, 0, 0).toDate();;
  @InjectMocks
  private AbsenceFacade facade;

  @Mock
  private AbsenceService absenceService;
  @Mock
  private AbsenceDTO absenceDTO;

  @BeforeMethod
  public void setUp() {
    facade = new AbsenceFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getUserAbsences() {
    when(absenceService.getUserAbsences(USER_ID, GROUP_ID)).thenReturn(singletonList(absenceDTO));

    final List<AbsenceDTO> result = facade.getUserAbsences(USER_ID, GROUP_ID);

    assertEquals(result, singletonList(absenceDTO));
  }

  public void getGroupAbsences() {
    when(absenceService.getGroupAbsences(GROUP_ID)).thenReturn(singletonList(absenceDTO));

    final List<AbsenceDTO> result = facade.getGroupAbsences(GROUP_ID);

    assertEquals(result, singletonList(absenceDTO));
  }

  public void saveAbsence() {
    when(absenceDTO.getId()).thenReturn(null);

    facade.saveAbsence(absenceDTO);

    verify(absenceService, times(1)).saveAbsence(eq(absenceDTO));
  }

  public void updateAbsence() {
    when(absenceDTO.getId()).thenReturn(2);

    facade.saveAbsence(absenceDTO);

    verify(absenceService, times(1)).updateAbsence(eq(absenceDTO));
  }

  public void deleteAbsence() {
    facade.deleteAbsence(2);

    verify(absenceService, times(1)).deleteAbsence(eq(2));
  }

  public void getAbsenceById() {
    when(absenceService.getAbsenceById(2)).thenReturn(absenceDTO);

    final AbsenceDTO result = facade.getAbsenceById(2);

    assertEquals(result, absenceDTO);
  }

  public void findGroupAbsencesBetweenTimespan() {
    when(absenceService.findGroupAbsencesBetweenTimespan(GROUP_ID, START, END)).thenReturn(singletonList(absenceDTO));

    final List<AbsenceDTO> result = facade.findGroupAbsencesBetweenTimespan(GROUP_ID, START, END);

    assertEquals(result, singletonList(absenceDTO));
  }
}
