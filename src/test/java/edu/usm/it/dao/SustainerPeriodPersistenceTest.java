package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.DonorInfo;
import edu.usm.domain.SustainerPeriod;
import edu.usm.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Created by andrew on 1/23/16.
 */
public class SustainerPeriodPersistenceTest extends WebAppConfigurationAware{


    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    ContactDao contactDao;

    private DonorInfo donorInfo;
    private Contact contact;

    private SustainerPeriod sustainerPeriod;

    @After
    public void tearDown() {
        contactDao.deleteAll();
        donorInfoDao.deleteAll();
    }

    @Before
    public void setup() {
        donorInfo = new DonorInfo();
        donorInfo.setCurrentSustainer(true);

        contact = new Contact();
        contact.setFirstName("Test");
        contact.setEmail("Test Email");

        sustainerPeriod = new SustainerPeriod();
        sustainerPeriod.setPeriodYear(2015);
        sustainerPeriod.setActive(true);
        sustainerPeriod.setJanuary(true);

        sustainerPeriod.setDonorInfo(donorInfo);
        donorInfo.addSustainerPeriod(sustainerPeriod);
        contact.setDonorInfo(donorInfo);

    }

    @Test
    public void testCreateSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        assertNotNull(contact.getDonorInfo());
        assertNotNull(contact.getDonorInfo().getSustainerPeriods());
        assertNotNull(contact.getDonorInfo().getSustainerPeriods().iterator().next());
    }

    @Test


}
