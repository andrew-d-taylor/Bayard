package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.*;
import edu.usm.dto.IdDto;
import edu.usm.dto.Response;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import edu.usm.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrganizationService organizationService;


    private Logger logger = LoggerFactory.getLogger(ContactController.class);


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getContacts() {
        return contactService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response createContact(@RequestBody Contact contact) {
        logger.debug("POST request to /contacts");
        String id;
        try {
            id = (contactService.create(contact));

        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Unable to create contact");
        }

        return new Response(id,Response.SUCCESS,null);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) {
        logger.debug("GET request to /contacts/contact/"+id);
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes={"application/json"})
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact contact) {
        logger.debug("PUT request to /contacts/contact/"+id);

        try {
            contactService.update(contact);
            return new Response(contact.getId(), Response.SUCCESS, null);

        } catch (Exception e) {
            logger.error(e.toString());
            return new Response(contact.getId(),Response.FAILURE,"Unable to update contact");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/events")
    public void attendEvent(@PathVariable("id") String id, @RequestBody IdDto eventId) {
        logger.debug("POST to /contacts/"+id+"/events");
        Contact contact = contactService.findById(id);
        Event event = eventService.findById(eventId.getId());

        if (null == contact.getAttendedEvents()) {
            contact.setAttendedEvents(new HashSet<>());
        }

        if (!contact.getAttendedEvents().contains(event)) {
            contactService.attendEvent(contact, event);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
    @JsonView(Views.EventList.class)
    public Set<Event> getAttendedEvents(@PathVariable("id") String id) {
        Contact contact = contactService.findById(id);
        return contact.getAttendedEvents();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public List<Encounter> getAllEncountersForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getEncounters();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/initiators")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getAllInitiators() {
        return contactService.findAllInitiators();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/organizations")
    @JsonView(Views.ContactOrganizationDetails.class)
    public Set<Organization> getAllOrganizationsForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getOrganizations();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/organizations", consumes= {"application/json"})
    public Response addContactToOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) {

        String idStringed = idDto.getId();

        Organization organization = organizationService.findById(idStringed);
        Contact contact = contactService.findById(id);

        if (organization == null) {
            logger.debug("No org");
            return new Response(null,Response.FAILURE, "Organization with ID " + idStringed + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return new Response(null,Response.FAILURE, "Contact with ID " + idStringed + " does not exist");
        }

        try {
            contactService.addContactToOrganization(contact,organization);
            return new Response(null, Response.SUCCESS, null);

        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to add contact with ID " + contact.getId() +
                    " to organization with ID " + organization.getId());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/organizations", consumes= {"application/json"})
    public Response removeContactFromOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) {
        String idStringed = idDto.getId();

        Organization organization = organizationService.findById(idStringed);
        Contact contact = contactService.findById(id);

        if (organization == null) {
            logger.debug("No org");
            return new Response(null,Response.FAILURE, "Organization with ID " + idStringed + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return new Response(null,Response.FAILURE, "Contact with ID " + idStringed + " does not exist");
        }

        try {
            contactService.removeContactFromOrganization(contact,organization);
            return new Response(null, Response.SUCCESS, null);

        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to remove contact with ID " + contact.getId() +
                    " from organization with ID " + organization.getId());
        }

    }



}

