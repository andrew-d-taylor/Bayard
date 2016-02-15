package edu.usm.it.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.service.FoundationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationControllerTest extends WebAppConfigurationAware {

    @Autowired
    FoundationService foundationService;

    Foundation foundation;

    ObjectMapper mapper = new ObjectMapper();

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
        String json = mapper.writeValueAsString(foundation);
        mockMvc.perform(post("/foundations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        Foundation fromDb = foundationService.findAll().iterator().next();
        assertNotNull(fromDb);
        assertEquals(foundation.getName(), fromDb.getName());
    }

    @Test
    public void testGetAllFoundations() throws Exception {
        foundationService.create(foundation);
        Foundation secondFoundation = new Foundation("Second Foundation");
        secondFoundation.setPrimaryContactEmail("primary@contact.email");
        secondFoundation.setAddress("987 Ave Americas");
        secondFoundation.setPrimaryContactName("Another Primary Contact");
        secondFoundation.setPhoneNumber("123-321-2121");
        foundationService.create(secondFoundation);


        mockMvc.perform(get("/foundations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder(foundation.getName(), secondFoundation.getName())))
                .andExpect(jsonPath("$.[*].address", containsInAnyOrder(foundation.getAddress(), secondFoundation.getAddress())))
                .andExpect(jsonPath("$.[*].primarycontactname", containsInAnyOrder(foundation.getPrimaryContactName(), secondFoundation.getPrimaryContactName())));


    }


}
