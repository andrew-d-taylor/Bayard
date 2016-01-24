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

import java.time.Month;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by andrew on 1/23/16.
 */
public class SustainerPeriodPersistenceTest extends WebAppConfigurationAware{


    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    ContactDao contactDao;

    @Autowired
    SustainerPeriodDao sustainerPeriodDao;

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
    public void testUpdateSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();
        int newAmount = sustainerPeriod.getMonthlyAmount() + 1;
        sustainerPeriod.setMonthlyAmount(newAmount);
        contactDao.save(contact);

        contact = contactDao.findOne(contact.getId());
        assertEquals(newAmount, contact.getDonorInfo().getSustainerPeriods().iterator().next().getMonthlyAmount());

    }

    @Test
    public void testDeleteSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());
        assertNotNull(contact);

        contactDao.delete(contact);

        contact = contactDao.findOne(contact.getId());
        assertNull(contact);

        donorInfo = donorInfoDao.findOne(donorInfo.getId());
        assertNull(donorInfo);

        sustainerPeriod = sustainerPeriodDao.findOne(sustainerPeriod.getId());
        assertNull(sustainerPeriod);
    }

    @Test
    public void testSustainerPeriodsOrdering() {

        SustainerPeriod olderSustainerPeriod = new SustainerPeriod();
        olderSustainerPeriod.setMonthlyAmount(1);
        olderSustainerPeriod.setPeriodYear(sustainerPeriod.getPeriodYear() - 1);
        olderSustainerPeriod.setDonorInfo(donorInfo);
        donorInfo.addSustainerPeriod(olderSustainerPeriod);

        SustainerPeriod mostRecentPeriod = new SustainerPeriod();
        mostRecentPeriod.setMonthlyAmount(2);
        mostRecentPeriod.setPeriodYear(sustainerPeriod.getPeriodYear() + 1);
        mostRecentPeriod.setDonorInfo(donorInfo);
        donorInfo.addSustainerPeriod(mostRecentPeriod);

        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());
        Iterator<SustainerPeriod> it = contact.getDonorInfo().getSustainerPeriods().iterator();

        SustainerPeriod first = it.next();
        assertEquals(mostRecentPeriod, first);

        SustainerPeriod second = it.next();
        assertEquals(sustainerPeriod, second);

        SustainerPeriod third = it.next();
        assertEquals(olderSustainerPeriod, third);

    }

    @Test
    public void testCalculateYearToDate() {
        sustainerPeriod.setJanuary(true);
        sustainerPeriod.setFebruary(true);
        sustainerPeriod.setDecember(true);
        assertEquals(sustainerPeriod.getMonthlyAmount() * 3, sustainerPeriod.getTotalYearToDate());

        sustainerPeriod.setDecember(false);
        assertEquals(sustainerPeriod.getMonthlyAmount() * 2, sustainerPeriod.getTotalYearToDate());

        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        assertEquals(sustainerPeriod.getMonthlyAmount() * 2, contact.getDonorInfo().getSustainerPeriods().iterator().next().getTotalYearToDate());
    }

    @Test
    public void testDynamicPeriodUpdate() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();
        sustainerPeriod.setMonth(Month.APRIL, true);
        sustainerPeriod.setMonth(Month.DECEMBER, true);
        contactDao.save(contact);

        contact = contactDao.findOne(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();

        assertTrue(sustainerPeriod.isApril());
        assertTrue(sustainerPeriod.isDecember());
        assertEquals(sustainerPeriod.getMonthlyAmount() * 2, sustainerPeriod.getTotalYearToDate());

    }

}
