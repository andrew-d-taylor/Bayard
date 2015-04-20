package edu.usm.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by scottkimball on 3/12/15.
 */

public class ContactControllerTest extends WebAppConfigurationAware {


    @Autowired
    ContactService contactService;

    private Logger logger = LoggerFactory.getLogger(ContactControllerTest.class);

    @Before
    public void setup() {

    }

    @After
    public void teardown () {
        contactService.deleteAll();
    }

    @Test
    public void testSerialization() throws Exception {
        Contact testContact = createTestContact();
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testContact);
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testPostContact() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String postData = mapper.writeValueAsString(createTestContact());
        logger.debug("Performing POST test");
        mockMvc.perform(post("/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postData.getBytes())).andExpect(status().isOk());
    }


    private Contact createTestContact() {
        Contact c = new Contact();
        c.setFirstName("First");
        c.setMiddleName("Middle");
        c.setLastName("Last");
        c.setStreetAddress("123 Fake St");
        c.setAptNumber("# 4");
        c.setCity("Portland");
        c.setZipCode("04101");
        c.setEmail("email@gmail.com");
        return c;

    }


    @Test
    @Transactional
    public void testGetAllContacts () throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        contactService.create(contact);
        String id = contact.getId();

        mockMvc.perform(get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    public void testGetContact() throws Exception {
        /*Basic info*/
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        /*Event*/
        Event event = new Event();
        event.setDate(LocalDate.of(2015, 01, 01));
        event.setLocation("location");
        event.setNotes("notes");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        event.setAttendees(contacts);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);

        /*Donations*/
        Donation donation = new Donation();
        donation.setDate(LocalDate.of(2015, 01, 01));
        donation.setAmount(100);
        donation.setComment("comment");



        /*DonorInfo*/
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.setContact(contact);
        donorInfo.setDate(LocalDate.of(2015, 01, 01));

        List<Donation> donations = new ArrayList<>();
        donations.add(donation);
        donorInfo.setDonations(donations);
        contact.setDonorInfo(donorInfo);

        /*Member Info*/
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setContact(contact);
        memberInfo.setStatus(0);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);
        contact.setMemberInfo(memberInfo);

        /*Organization*/
        Organization organization = new Organization();
        organization.setName("organization");
        organization.setMembers(contacts);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);

        contactService.create(contact);
        String id = contact.getId();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate4Module());


        mockMvc.perform(get("/contacts/contact/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


}
