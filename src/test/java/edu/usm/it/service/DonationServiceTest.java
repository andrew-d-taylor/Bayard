package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Donation;
import edu.usm.service.DonationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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
    }

    @Autowired
    public void tearDown() {
        donationService.deleteAll();
    }

    @Test
    public void testCreateDonation() {

    }

    @Test
    public void testUpdateDonation() {

    }

    @Test
    public void testDeleteDonation() {


    }

}
