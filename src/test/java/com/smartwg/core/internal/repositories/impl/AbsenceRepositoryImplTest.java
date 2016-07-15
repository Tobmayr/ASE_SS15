package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.repositories.AbsenceRepository;
import com.smartwg.core.util.Constants;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class AbsenceRepositoryImplTest {

  private static final Integer USER_ID = 2;
  private static final Integer GROUP_ID = 3;
  private static final Integer ABSENCE_ID = 4;

  @InjectMocks
  private AbsenceRepository repository;

  @Mock
  private EntityManager entityManager;

  @Mock
  private Query query;
  @Mock
  private AbsenceDTO absenceDTO;

  @BeforeMethod
  public void setUp() {
    repository = new AbsenceRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findUserAbsences() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ABSENCES_USERID)))
        .thenReturn(query);
    when(query.setParameter("userId", USER_ID)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(absenceDTO));

    final List<AbsenceDTO> result = repository.findUserAbsences(USER_ID, GROUP_ID);

    assertEquals(result, Collections.singletonList(absenceDTO));
  }

  public void findGroupAbsences() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ABSENCES_GROUP))).thenReturn(query);
    when(query.setParameter("groupId", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(absenceDTO));

    final List<AbsenceDTO> result = repository.findGroupAbsences(GROUP_ID);

    assertEquals(result, Collections.singletonList(absenceDTO));
  }

  public void findAbsenceById() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ABSENCE_ID))).thenReturn(query);
    when(query.setParameter("absenceId", ABSENCE_ID)).thenReturn(query);
    when(query.getSingleResult()).thenReturn(absenceDTO);

    final AbsenceDTO result = repository.findAbsenceById(ABSENCE_ID);

    assertEquals(result, absenceDTO);
  }
}
