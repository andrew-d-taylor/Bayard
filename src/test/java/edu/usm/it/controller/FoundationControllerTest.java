package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Views;
import edu.usm.service.FoundationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationControllerTest extends WebAppConfigurationAware {

    @Autowired
    FoundationService foundationService;

    final String FOUNDATIONS_BASE_URL = "/foundations/";

    Foundation foundation;

    @Before
    public void setup() {
        foundation = new Foundation("Test Foundation");
        foundation.setCurrentGrantor(true);
        foundation.setPhoneNumber("123-123-1233");
        foundation.setAddress("123 Lane Ave");
        foundation.setPrimaryContactEmail("email@email.com");
        foundation.setPrimaryContactName("Point Person");
        foundation.setPrimaryContactEmail("point@person.com");
        foundation.setPrimaryContactTitle("Manager");
        foundation.setPrimaryContactPhone("123-123-1111");
        foundation.setSecondaryContactName("Secondary Point Person");
        foundation.setSecondaryContactTitle("Assistant Regional Manager");
        foundation.setSecondaryContactEmail("second@email.com");
        foundation.setSecondaryContactPhone("123-123-0000");

    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateFoundation() throws Exception {
        BayardTestUtilities.performEntityPost(FOUNDATIONS_BASE_URL, foundation, mockMvc);
        Foundation fromDb = foundationService.findAll().iterator().next();
        assertNotNull(fromDb);
        assertEquals(foundation.getName(), fromDb.getName());
    }

    @Test
    public void testGetFoundation() throws Exception {
        foundationService.create(foundation);
        BayardTestUtilities.performEntityGetSingle(Views.FoundationDetails.class, FOUNDATIONS_BASE_URL + foundation.getId(), mockMvc, foundation);
    }

    @Test
    public void testGetAllFoundations() throws Exception {
        foundationService.create(foundation);
        Foundation secondFoundation = makeSecondFoundation();
        foundationService.create(secondFoundation);

        BayardTestUtilities.performEntityGetMultiple(Views.FoundationList.class, FOUNDATIONS_BASE_URL, mockMvc, foundation, secondFoundation);
    }

    private Foundation makeSecondFoundation() {
        Foundation secondFoundation = new Foundation("Second Foundation");
        secondFoundation.setPrimaryContactEmail("primary@contact.email");
        secondFoundation.setAddress("987 Ave Americas");
        secondFoundation.setPrimaryContactName("Another Primary Contact");
        secondFoundation.setPhoneNumber("123-321-2121");
        return secondFoundation;
    }

    @Test
    public void testUpdateFoundation() throws Exception{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        String newFoundationName = "New Name for the Foundation";
        foundation.setName(newFoundationName);

        BayardTestUtilities.performEntityPut(FOUNDATIONS_BASE_URL + foundation.getId(), foundation, mockMvc);

        Foundation fromDb = foundationService.findById(foundation.getId());
        assertEquals(newFoundationName, fromDb.getName());
    }

    @Test
    public void testDeleteFoundation() throws Exception {
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        BayardTestUtilities.performEntityDelete(FOUNDATIONS_BASE_URL + foundation.getId(), mockMvc);

        foundation = foundationService.findById(foundation.getId());
        assertNull(foundation);
    }

}
