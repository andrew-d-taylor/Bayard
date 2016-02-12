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
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


}
