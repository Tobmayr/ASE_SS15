package com.smartwg.core.internal.repositories.impl;

import static com.smartwg.core.util.Constants.QUERY_ALL_GROUPS;
import static com.smartwg.core.util.Constants.QUERY_ALL_GROUPS_BY_USER;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.repositories.GroupRepository;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class GroupRepositoryImplTest {

  private static final Integer GROUP_ID = 2;
  private static final Integer USER_ID = 3;

  @InjectMocks
  private GroupRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;

  @Mock
  private GroupDTO groupDTO;

  @BeforeMethod
  public void setUp() {
    repository = new GroupRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findGroups() {
    when(entityManager.createNamedQuery(QUERY_ALL_GROUPS)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(groupDTO));

    final List<GroupDTO> result = repository.findGroups();

    assertEquals(result, singletonList(groupDTO));
  }

  public void findGroupsByUserID() {
    when(entityManager.createNamedQuery(QUERY_ALL_GROUPS_BY_USER)).thenReturn(query);
    when(query.setParameter("userid", USER_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(groupDTO));

    final List<GroupDTO> result = repository.findGroupsByUserID(USER_ID);

    assertEquals(result, singletonList(groupDTO));
  }

  public void isAdmin() {
    when(
        entityManager.createQuery("SELECT ug FROM UserGroup ug WHERE ug.group.id=" + GROUP_ID
            + " AND ug.user.id =" + USER_ID + " AND role='ADMIN'")).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(groupDTO));

    final boolean result = repository.isAdmin(GROUP_ID, USER_ID);

    assertTrue(result);
  }

  public void searchGroups() {
    when(
        entityManager
            .createQuery("SELECT g FROM Group g WHERE g.name like :param OR g.country.name like :param OR g.city "
                + "like :param OR g.zip like :param OR g.street like :param OR g.street2 like :param"))
        .thenReturn(query);
    when(query.setParameter("param", "test")).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(groupDTO));

    final List<GroupDTO> result = repository.searchGroups("test");

    assertEquals(result, singletonList(groupDTO));
  }
}
