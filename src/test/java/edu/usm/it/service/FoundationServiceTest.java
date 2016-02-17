package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ConcurrentModificationException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationServiceTest extends WebAppConfigurationAware{

    @Autowired
    FoundationService foundationService;

    @Autowired
    GrantService grantService;

    Foundation foundation;
    Grant grant;

    @Before
    public void setup() {
        foundation = new Foundation("Foundation Name");
        foundation.setAddress("123 Lane Ave");
        foundation.setCurrentGrantor(false);
        foundation.setPhoneNumber("123-321-412");

        grant = new Grant();
        grant.setName("Test Grant");
        grant.setFoundation(foundation);
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);
        grant = grantService.findById(grant.getId());
        assertNotNull(grant);
    }

    @Test
    public void testUpdateFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        String newName = "New Name";
        foundation.setName(newName);
        foundationService.update(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertEquals(newName, foundation.getName());
    }

    @Test
    public void testDeleteFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        foundationService.delete(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNull(foundation);

        grant = grantService.findById(grant.getId());
        assertNull(grant);
    }

    @Test
    public void testDeleteAll() throws ConstraintViolation{
        foundationService.create(foundation);
        Foundation second = new Foundation("Second Foundation");
        foundationService.create(second);
        foundation = foundationService.findById(foundation.getId());
        second = foundationService.findById(second.getId());
        assertNotNull(foundation);
        assertNotNull(second);

        foundationService.deleteAll();
        Set<Foundation> shouldBeEmpty = foundationService.findAll();
        assertTrue(shouldBeEmpty.isEmpty());

        Set<Grant> shouldAlsoBeEmpty = grantService.findAll();
        assertTrue(shouldAlsoBeEmpty.isEmpty());
    }

}
