package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.UserFileUpload;
import edu.usm.repository.FoundationDao;
import edu.usm.repository.GrantDao;
import edu.usm.repository.InteractionRecordDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 1/24/16.
 */
public class FoundationPersistenceTest extends WebAppConfigurationAware {

    @Autowired
    private FoundationDao foundationDao;

    @Autowired
    private GrantDao grantDao;

    @Autowired
    private InteractionRecordDao interactionRecordDao;

    private Foundation foundation;
    private Grant grant;
    private InteractionRecord interactionRecord;
    private UserFileUpload userFileUpload;

    private byte[] fileData;
    private int fileLength;

    public FoundationPersistenceTest() {
        File testFile = new File("application.properties");
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            fileLength = (int)testFile.length();
            fileData = new byte[fileLength];
            fileInputStream.read(fileData);
        } catch (IOException e) {
            fileLength = 10;
            fileData = new byte[fileLength];
        }
    }

    @Before
    public void setup() throws Exception {

        foundation = new Foundation();
        foundation.setName("Test Foundation");

        grant = new Grant();
        grant.setName("Test Grant");
        grant.setFoundation(foundation);
        grant.setAmountAppliedFor(100);
        grant.setAmountReceived(50);

        userFileUpload = new UserFileUpload();
        userFileUpload.setFileContent(fileData);
        userFileUpload.setFileType(".properties");
        userFileUpload.setFileName("application.properties");
        userFileUpload.setDescription("A test file");
        grant.addFileUpload(userFileUpload);

        foundation.addGrant(grant);

        interactionRecord = new InteractionRecord();
        interactionRecord.setDateOfInteraction(LocalDate.now());
        interactionRecord.setFoundation(foundation);
        foundation.addInteractionRecord(interactionRecord);

    }

    @After
    public void teardown() {
        foundationDao.deleteAll();
        grantDao.deleteAll();
        interactionRecordDao.deleteAll();
    }

    @Test
    public void testCreateFoundation() {
        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        assertNotNull(foundation);
        assertEquals(interactionRecord, foundation.getInteractionRecords().iterator().next());
        assertEquals(grant, foundation.getGrants().iterator().next());
        assertEquals(userFileUpload, grant.getFileUploads().iterator().next());
        assertEquals(fileData, grant.getFileUploads().iterator().next().getFileContent());
    }

    @Test
    public void testDeleteFoundation() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        foundationDao.delete(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        assertNull(foundation);

        grant = grantDao.findOne(grant.getId());
        assertNull(grant);

        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());
        assertNull(interactionRecord);
    }

    @Test
    public void testCascadeUpdateGrant() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        grant = foundation.getGrants().iterator().next();
        int newAmount = grant.getAmountAppliedFor() + 1;
        grant.setAmountAppliedFor(newAmount);

        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        grant = foundation.getGrants().iterator().next();
        assertEquals(newAmount, grant.getAmountAppliedFor());

    }

    @Test
    public void testUpdateGrant() {
        foundationDao.save(foundation);

        grant = grantDao.findOne(grant.getId());
        int newAmount = grant.getAmountAppliedFor() + 1;
        grant.setAmountAppliedFor(newAmount);

        grantDao.save(grant);
        grant = grantDao.findOne(grant.getId());

        assertEquals(newAmount, grant.getAmountAppliedFor());
    }

    @Test
    public void testCascadeUpdateInteractionRecord() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        interactionRecord = foundation.getInteractionRecords().iterator().next();
        String newInteractionType = "Test Type";
        interactionRecord.setInteractionType(newInteractionType);

        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        interactionRecord = foundation.getInteractionRecords().iterator().next();
        assertEquals(newInteractionType, interactionRecord.getInteractionType());
    }

    @Test
    public void testUpdateInteractionRecord() {
        foundationDao.save(foundation);
        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());

        String newInteractionType = "Test Type";
        interactionRecord.setInteractionType(newInteractionType);
        interactionRecordDao.save(interactionRecord);

        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());
        assertEquals(newInteractionType, interactionRecord.getInteractionType());
    }

}
