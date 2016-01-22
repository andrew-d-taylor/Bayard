package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.DonorInfo;
import edu.usm.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 1/22/16.
 */
public class DonationsTest extends WebAppConfigurationAware {

    @Autowired
    DonationDao donationDao;

    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    ContactDao contactDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    EventDao eventDao;

    private Donation donation;

    @After
    public void tearDown() {
        contactDao.deleteAll();
        donorInfoDao.deleteAll();
        donationDao.deleteAll();
        eventDao.deleteAll();
        organizationDao.deleteAll();
    }

    @Before
    public void setup() {
        donation = new Donation();
        donation.setAmount(100);
        donation.setBudgetItem("Budget Item");
        donation.setRestrictedToCategory("Restricted To");
        donation.setStandalone(true);
        donation.setAnonymous(true);
        donation.setDateOfDeposit(LocalDate.of(2016, 6, 10));
        donation.setDateOfReceipt(LocalDate.of(2016, 6, 7));

    }

    @Test
    public void testCreateDonation() {
        donationDao.save(donation);
        Donation fromDb = donationDao.findOne(donation.getId());
        assertEquals(donation.getAmount(), fromDb.getAmount());
        assertEquals(donation.getBudgetItem(), fromDb.getBudgetItem());
        assertEquals(donation.getRestrictedToCategory(), fromDb.getRestrictedToCategory());
        assertEquals(donation.isStandalone(), fromDb.isStandalone());
        assertEquals(donation.isAnonymous(), fromDb.isAnonymous());
        assertEquals(donation.getDateOfReceipt(), fromDb.getDateOfReceipt());
        assertEquals(donation.getDateOfDeposit(), fromDb.getDateOfDeposit());
    }

    @Test
    public void testDeleteDonation() {
        donationDao.save(donation);
        donationDao.delete(donation);
        Donation fromDb = donationDao.findOne(donation.getId());
        assertNull(fromDb);
    }

    @Test
    public void testCreateDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        DonorInfo fromDb = donorInfoDao.findOne(donorInfo.getId());
        assertEquals(fromDb.getDonations().iterator().next(), donation);
    }

    @Test
    public void testDeleteDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        donorInfoDao.delete(donorInfo);

        DonorInfo fromDb = donorInfoDao.findOne(donorInfo.getId());
        assertNull(fromDb);

        Donation relatedDonation = donationDao.findOne(donation.getId());
        assertNull(relatedDonation);
    }

    @Test
    public void testCreateContactWithDonations() {
        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);

        Contact contactFromDb = contactDao.findOne(c.getId());
        Set<Donation> contactDonations = contactFromDb.getDonorInfo().getDonations();
        assertEquals(contactDonations.iterator().next(), donation);
    }

    @Test
    public void testDeleteContactWithDonations() {
        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);
        Contact fromDb = contactDao.findOne(c.getId());
        assertNotNull(fromDb);
        contactDao.delete(c);

        fromDb = contactDao.findOne(c.getId());
        assertNull(fromDb);
        Donation donationFromDb = donationDao.findOne(donation.getId());
        assertNull(donationFromDb);
    }

    

}
