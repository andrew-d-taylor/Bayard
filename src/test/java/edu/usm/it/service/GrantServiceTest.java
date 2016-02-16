package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

/**
 * Created by andrew on 2/16/16.
 */
public class GrantServiceTest extends WebAppConfigurationAware {

    @Autowired
    GrantService grantService;

    @Autowired
    FoundationService foundationService;

    Foundation foundation;
    Grant grant;

    @Before
    public void setup() throws Exception{
        foundation = new Foundation("Test Foundation");
        foundationService.create(foundation);

        grant = new Grant("Test Grant", foundation);
        grant.setAmountAppliedFor(100);
        grant.setAmountReceived(100);
        grant.setDescription("A test grant");
        grant.setReportDeadline(LocalDate.of(2016, 7, 1));
        grant.setApplicationDeadline(LocalDate.of(2015, 3, 1));
        grant.setIntentDeadline(LocalDate.of(2015, 2, 1));
        grant.setStartPeriod(LocalDate.of(2015, 6, 1));
        grant.setEndPeriod(LocalDate.of(2016, 6, 1));
        grant.setRestriction("Administration expenses");
    }

    @After
    public void teardown() {
        grantService.deleteAll();
        foundationService.deleteAll();
    }

    @Test
    public void testCreateGrant() {
        grantService.create(grant);
        grant = grantService.findById(grant.getId());
        assertNotNull(grant);
    }

}
