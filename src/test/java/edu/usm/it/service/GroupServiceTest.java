package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link GroupService}
 */
public class GroupServiceTest extends WebAppConfigurationAware{

    @Autowired
    private GroupService groupService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EventService eventService;

    private Group group;
    private Committee committee;
    private Organization organization;
    private Event event;

    private Contact first;
    private Contact second;
    private Contact topLevel;

    @Before
    public void setup() throws ConstraintViolation, NullDomainReference{
        createContacts();
        committee = new Committee();
        committee.setName("Committee Name");
        committeeService.create(committee);
        contactService.addContactToCommittee(first.getId(), committee.getId());
        contactService.addContactToCommittee(second.getId(), committee.getId());

        organization = new Organization();
        organization.setName("Organization Name");
        organizationService.create(organization);
        organization = organizationService.findById(organization.getId());
        contactService.addContactToOrganization(first.getId(), organization.getId());
        contactService.addContactToOrganization(second.getId(), organization.getId());

        event = new Event();
        event.setName("Event Name");
        event.setDateHeld("2015-01-01");
        eventService.create(event);
        contactService.attendEvent(first.getId(), event.getId());
        contactService.attendEvent(second.getId(), event.getId());

        group = new Group();
        group.setGroupName("New AlinskyGroup");

    }

    private void createContacts() throws ConstraintViolation{
        first = new Contact();
        first.setFirstName("First");
        first.setEmail("email@emememe.com");

        second = new Contact();
        second.setFirstName("Second");
        second.setEmail("second@email.com");

        topLevel = new Contact();
        topLevel.setFirstName("Top Level");
        topLevel.setEmail("email@email.com");

        contactService.create(first);
        contactService.create(second);
        contactService.create(topLevel);
    }

    @After
    public void tearDown() {
        groupService.deleteAll();
        organizationService.deleteAll();
        committeeService.deleteAll();
        eventService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    public void testCreateGroupFromCommittee() throws Exception{
        committeeService.create(committee);
        committee = committeeService.findById(committee.getId());
        String id = groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        Aggregation aggCommittee = committeeService.findById(aggregation.getId());
        assertNotNull(aggCommittee);
        assertEquals(committee.getAggregationMembers().size(), aggCommittee.getAggregationMembers().size());
    }

    @Test
    public void testCreateGroupFromOrganization() throws Exception{
        String id = groupService.create(group);
        eventService.create(event);
        groupService.addAggregation(organization.getId(), group.getId());

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        Aggregation aggOrg = organizationService.findById(aggregation.getId());
        int aggSize = aggOrg.getAggregationMembers().size();
        int orgSize = organizationService.findById(organization.getId()).getMembers().size();
        assertNotNull(aggregation);

        assertEquals(aggSize, orgSize);
    }

    @Test
    public void testCreateGroupFromEvent() throws Exception{
        String id = groupService.create(group);
        eventService.create(event);
        groupService.addAggregation(event.getId(), group.getId());

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = eventService.findById(fromDb.getAggregations().iterator().next().getId());
        assertNotNull(aggregation);
        assertEquals(1, aggregation.getGroups().size());
        event = eventService.findById(event.getId());
        assertEquals(event.getAggregationMembers().size(), aggregation.getAggregationMembers().size());
    }

    @Test
    @Transactional
    public void testCreateGroupFromMultipleAggregations() throws Exception{
        String id = groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());
        group = groupService.findById(group.getId());
        groupService.addAggregation(organization.getId(), group.getId());
        group = groupService.findById(group.getId());
        groupService.addAggregation(event.getId(), group.getId());
        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel.getId(), group.getId());

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        assertEquals(3, group.getAggregations().size());
        Set<Contact> allAggregationMembers = new HashSet<>();
        for (Aggregation aggregation: group.getAggregations()) {
            for (Contact c: aggregation.getAggregationMembers()) {
                allAggregationMembers.add(c);
            }
        }
        assertEquals(2, allAggregationMembers.size());
        assertEquals(1, group.getTopLevelMembers().size());
    }

    @Test(expected = NullDomainReference.NullGroup.class)
    public void testDeleteGroup() throws Exception{
        committeeService.create(committee);
        committee = committeeService.findById(committee.getId());
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());
        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel.getId(), group.getId());
        groupService.delete(group.getId());

        Group groupFromDb = groupService.findById(group.getId());
        assertNull(groupFromDb);
        Aggregation fromDb = committeeService.findById(committee.getId());
        assertNotNull(fromDb);
        assertEquals(0, fromDb.getGroups().size());
        assertEquals(committee.getAggregationMembers().size(), fromDb.getAggregationMembers().size());

        Contact topLevelFromDb = contactService.findById(topLevel.getId());
        assertNotNull(topLevelFromDb);
        assertEquals(0, topLevelFromDb.getGroups().size());
    }

    @Test
    public void testAddMultipleContacts() throws Exception{
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());

        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel.getId(), group.getId());

        Contact anotherContact = new Contact();
        anotherContact.setFirstName("Another");
        anotherContact.setEmail("another@email.com");
        contactService.create(anotherContact);

        contactService.addToGroup(anotherContact.getId(), group.getId());
        group = groupService.findById(group.getId());
        assertEquals(2, group.getTopLevelMembers().size());

        anotherContact = contactService.findById(anotherContact.getId());
        topLevel = contactService.findById(topLevel.getId());

        Set<Group> anotherContactGroups = contactService.getAllContactGroups(anotherContact.getId());
        Set<Group> topLevelGroups = contactService.getAllContactGroups(topLevel.getId());
        assertEquals(1, anotherContactGroups.size());
        assertEquals(1, topLevelGroups.size());

        groupService.delete(group.getId());

        anotherContact = contactService.findById(anotherContact.getId());
        topLevel = contactService.findById(topLevel.getId());
        anotherContactGroups = contactService.getAllContactGroups(anotherContact.getId());
        topLevelGroups = contactService.getAllContactGroups(topLevel.getId());

        assertEquals(0, anotherContactGroups.size());
        assertEquals(0, topLevelGroups.size());
    }

    @Test
    public void testRemoveContactFromGroup() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());
        contactService.addToGroup(topLevel.getId(), group.getId());
        group = groupService.findById(group.getId());

        assertEquals(1, group.getTopLevelMembers().size());

        contactService.removeFromGroup(topLevel.getId(), group.getId());
        Group fromDb = groupService.findById(group.getId());

        assertEquals(0, fromDb.getTopLevelMembers().size());

        Contact contactFromDb = contactService.findById(topLevel.getId());
        Set<Group> groups = contactService.getAllContactGroups(contactFromDb.getId());
        assertEquals(0, groups.size());
    }

    @Test
    public void testAddAggregation() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());

        Group fromDb = groupService.findById(group.getId());
        assertEquals(1, fromDb.getAggregations().size());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        Aggregation committee = committeeService.findById(aggregation.getId());
        assertEquals(group.getGroupName(), committee.getGroups().iterator().next().getGroupName());
    }

    @Test
    public void testRemoveAggregation() throws Exception {
        String id = groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());

        Group fromDb = groupService.findById(id);
        assertEquals(1, fromDb.getAggregations().size());
        Aggregation committee = committeeService.findById(fromDb.getAggregations().iterator().next().getId());
        assertEquals(1, committee.getGroups().size());

        Aggregation aggregation = committeeService.findById(committee.getId());
        assertEquals(1, aggregation.getGroups().size());

        groupService.removeAggregation(committee.getId(), fromDb.getId());

        fromDb = groupService.findById(id);
        assertEquals(0, fromDb.getAggregations().size());

        aggregation = committeeService.findById(committee.getId());
        assertEquals(0, aggregation.getGroups().size());

    }

    @Test
    public void testRemoveContactFromAggregation() throws Exception {
        committeeService.create(committee);
        committee = committeeService.findById(committee.getId());
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());
        group = groupService.findById(group.getId());

        Aggregation aggregation = group.getAggregations().iterator().next();
        Aggregation aggCommittee = committeeService.findById(aggregation.getId());
        assertEquals(committee.getMembers().size(),aggCommittee.getAggregationMembers().size());

        contactService.removeContactFromCommittee(first.getId(), committee.getId());
        committee = committeeService.findById(committee.getId());
        group = groupService.findById(group.getId());
        aggCommittee = committeeService.findById(aggregation.getId());
        assertEquals(committee.getMembers().size(), aggCommittee.getAggregationMembers().size());
    }

    @Test
    @Transactional
    public void testAddEventMultipleGroups() throws Exception {
        groupService.create(group);
        groupService.addAggregation(event.getId(), group.getId());
        groupService.addAggregation(committee.getId(), group.getId());

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(event.getId(), secondGroup.getId());
        groupService.addAggregation(organization.getId(), secondGroup.getId());

        event = eventService.findById(event.getId());
        assertTrue(event.getGroups().contains(group));
        assertTrue(event.getGroups().contains(secondGroup));

        group = groupService.findById(group.getId());
        assertTrue(group.getAggregations().contains(event));

        secondGroup = groupService.findById(secondGroup.getId());
        assertTrue(secondGroup.getAggregations().contains(event));
    }

    @Test
    @Transactional
    public void testAddContactToEventMultipleGroups() throws Exception {
        groupService.create(group);
        groupService.addAggregation(event.getId(), group.getId());

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(event.getId(), secondGroup.getId());

        Contact newContact = new Contact();
        newContact.setFirstName("Fresh Contact");
        newContact.setEmail("Fresh email");
        contactService.create(newContact);

        contactService.attendEvent(newContact.getId(), event.getId());

        event = eventService.findById(event.getId());
        assertTrue(event.getAttendees().contains(newContact));

        newContact = contactService.findById(newContact.getId());
        assertTrue(newContact.getAttendedEvents().contains(event));
    }

    @Test
    @Transactional
    public void testAddContactToOrganizationMultipleGroups() throws Exception {
        groupService.create(group);
        groupService.addAggregation(organization.getId(), group.getId());

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(organization.getId(), secondGroup.getId());

        Contact newContact = new Contact();
        newContact.setFirstName("Fresh Contact");
        newContact.setEmail("Fresh email");
        contactService.create(newContact);

        contactService.addContactToOrganization(newContact.getId(), organization.getId());

        newContact = contactService.findById(newContact.getId());
        assertTrue(newContact.getOrganizations().contains(organization));

        group = groupService.findById(group.getId());
        assertTrue(group.getAggregations().contains(organization));

        secondGroup = groupService.findById(secondGroup.getId());
        assertTrue(secondGroup.getAggregations().contains(organization));

        organization = organizationService.findById(organization.getId());
        assertTrue(organization.getMembers().contains(newContact));
    }

    @Test
    @Transactional
    public void testRemoveContactFromEventMultipleGroups() throws Exception {
        groupService.create(group);
        groupService.addAggregation(event.getId(), group.getId());

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(event.getId(), secondGroup.getId());

        Contact newContact = new Contact();
        newContact.setFirstName("Fresh Contact");
        newContact.setEmail("Fresh email");
        contactService.create(newContact);

        event = eventService.findById(event.getId());
        contactService.attendEvent(newContact.getId(), event.getId());

        newContact = contactService.findById(newContact.getId());
        contactService.unattendEvent(newContact.getId(), event.getId());

        event = eventService.findById(event.getId());
        assertFalse(event.getAttendees().contains(newContact));

        newContact = contactService.findById(newContact.getId());
        assertFalse(newContact.getAttendedEvents().contains(event));
    }

    @Test
    @Transactional
    public void testAddContactToGroupAndGroupConstituent() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());

        Contact contact = new Contact();
        contact.setFirstName("Test Contact");
        contact.setEmail("test@email.com");
        contactService.create(contact);

        contactService.addContactToCommittee(contact.getId(), committee.getId());
        contactService.addToGroup(contact.getId(), group.getId());

        contact = contactService.findById(contact.getId());
        assertTrue(contact.getGroups().contains(group));
        assertTrue(contact.getCommittees().contains(committee));

        committee = committeeService.findById(committee.getId());
        assertTrue(committee.getMembers().contains(contact));

        group = groupService.findById(group.getId());
        assertTrue(group.getTopLevelMembers().contains(contact));
    }

    @Test
    @Transactional
    public void testAddContactToMultipleGroupsMultipleConstituents() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee.getId(), group.getId());
        groupService.addAggregation(event.getId(), group.getId());

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(committee.getId(), secondGroup.getId());
        groupService.addAggregation(event.getId(), secondGroup.getId());

        Contact contact = new Contact();
        contact.setFirstName("Test Contact");
        contact.setEmail("test@email.com");
        contactService.create(contact);

        contactService.addContactToCommittee(contact.getId(), committee.getId());
        contactService.attendEvent(contact.getId(), event.getId());
        contactService.addToGroup(contact.getId(), group.getId());
        contactService.addToGroup(contact.getId(), secondGroup.getId());

        contact = contactService.findById(contact.getId());
        group = groupService.findById(group.getId());
        secondGroup = groupService.findById(secondGroup.getId());
        event = eventService.findById(event.getId());
        committee = committeeService.findById(committee.getId());

        assertTrue(contact.getGroups().contains(group));
        assertTrue(contact.getGroups().contains(secondGroup));
        assertTrue(contact.getCommittees().contains(committee));
        assertTrue(contact.getAttendedEvents().contains(event));

        assertTrue(event.getAttendees().contains(contact));
        assertTrue(event.getGroups().contains(group));
        assertTrue(event.getGroups().contains(secondGroup));

        assertTrue(committee.getMembers().contains(contact));
        assertTrue(committee.getGroups().contains(group));
        assertTrue(committee.getGroups().contains(secondGroup));

        assertTrue(group.getTopLevelMembers().contains(contact));
        assertTrue(group.getAggregations().contains(committee));
        assertTrue(group.getAggregations().contains(event));

        assertTrue(secondGroup.getTopLevelMembers().contains(contact));
        assertTrue(secondGroup.getAggregations().contains(committee));
        assertTrue(secondGroup.getAggregations().contains(event));
    }
}
