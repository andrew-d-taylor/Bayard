package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.service.FoundationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationServiceTest extends WebAppConfigurationAware{

    @Autowired
    FoundationService foundationService;

    Foundation foundation;

    @Before
    public void setup() {
        foundation = new Foundation("Foundation Name");
        foundation.setAddress("123 Lane Ave");
        foundation.setCurrentGrantor(false);
        foundation.setPhoneNumber("123-321-412");
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateFoundation() {
        foundationService.create(foundation);
    }

    @Test
    public void testUpdateFoundation() {
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        String newName = "New Name";
        foundation.setName(newName);
        foundationService.update(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertEquals(newName, foundation.getName());
    }

    @Test
    public void testDeleteFoundation() {
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        foundationService.delete(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNull(foundation);
    }

    @Test
    public void testDeleteAll() {
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
    }

}
