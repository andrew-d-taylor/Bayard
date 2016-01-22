package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Donation;
import edu.usm.domain.DonorInfo;
import edu.usm.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        donorInfoDao.deleteAll();
        donationDao.deleteAll();
        contactDao.deleteAll();
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
    public void testCreateDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        List<Donation> donations = new ArrayList<>();
        donations.add(donation);
        donorInfo.setDonations(donations);
        donorInfoDao.save(donorInfo);

        DonorInfo fromDb = donorInfoDao.findOne(donorInfo.getId());
        assertEquals(fromDb.getDonations().get(0), donation);
    }

}
