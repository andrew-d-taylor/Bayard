package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Donation;
import edu.usm.service.DonationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 2/25/16.
 */
public class DonationServiceTest extends WebAppConfigurationAware {

    @Autowired
    DonationService donationService;

    Donation donation;

    @Before
    public void setup() {
        donation = new Donation();
        donation.setAmount(20);
        donation.setAnonymous(true);
        donation.setBudgetItem("Misc");
        donation.setDateOfDeposit(LocalDate.of(2015, 12, 12));
        donation.setDateOfReceipt(LocalDate.of(2015, 11, 11));
        donation.setMethod("Credit Card");
        donation.setRestrictedToCategory("Office supplies");
        donation.setStandalone(true);
    }

    @Autowired
    public void tearDown() {
        donationService.deleteAll();
    }

    @Test
    public void testCreateDonation() {
        donationService.create(donation);
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
    }

    @Test
    public void testUpdateDonation() {
        donationService.create(donation);
        int newDonationAmount = donation.getAmount() + 10;
        donation.setAmount(newDonationAmount);
        donationService.update(donation);

        donation = donationService.findById(donation.getId());
        assertEquals(newDonationAmount, donation.getAmount());
    }

    @Test
    public void testDeleteDonation() {
        donationService.create(donation);
        donation = donationService.findById(donation.getId());

        donationService.delete(donation);
        donation = donationService.findById(donation.getId());

        assertNull(donation);
    }

}
