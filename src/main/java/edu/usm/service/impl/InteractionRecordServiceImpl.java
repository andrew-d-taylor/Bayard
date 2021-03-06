package edu.usm.service.impl;

import edu.usm.domain.Foundation;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.InteractionRecordType;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.InteractionRecordDto;
import edu.usm.repository.InteractionRecordDao;
import edu.usm.repository.InteractionRecordTypeDao;
import edu.usm.service.BasicService;
import edu.usm.service.FoundationService;
import edu.usm.service.InteractionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Set;

/**
 * Created by andrew on 2/18/16.
 */
@Service
public class InteractionRecordServiceImpl extends BasicService implements InteractionRecordService{

    @Autowired
    private InteractionRecordDao dao;

    @Autowired
    private InteractionRecordTypeDao typeDao;

    @Autowired
    private FoundationService foundationService;

    @Override
    public Set<InteractionRecord> findAll() {
        return dao.findAll();
    }

    @Override
    public InteractionRecord findById(String id) {
        return dao.findOne(id);
    }

    @Override
    public String create(InteractionRecord record) throws ConstraintViolation{
        try {
            updateLastModified(record);
            dao.save(record);
        } catch (DataAccessException | TransactionSystemException e) {
            handlePersistenceException(record);
        }
        return record.getId();
    }

    @Override
    public void update(InteractionRecord record) throws ConstraintViolation{
        try {
            updateLastModified(record);
            dao.save(record);
        } catch (DataAccessException | TransactionSystemException e) {
            handlePersistenceException(record);
        }
    }

    @Override
    public void update(InteractionRecord record, InteractionRecordDto details) throws ConstraintViolation {
        record = DtoTransformer.fromDto(details, record);
        if (null != details.getInteractionTypeId()) {
            record.setInteractionType(findInteractionRecordType(details.getInteractionTypeId()));
        }
        update(record);
    }

    private void handlePersistenceException(InteractionRecord record) throws ConstraintViolation {
        if (null == record.getPersonContacted() || record.getPersonContacted().isEmpty()) {
            throw new ConstraintViolation(ConstraintMessage.INTERACTION_RECORD_NO_CONTACT_PERSON);
        }
        if (null == record.getDateOfInteraction()) {
            throw new ConstraintViolation(ConstraintMessage.INTERACTION_RECORD_NO_DATE);
        }
        if (null == record.getFoundation()) {
            throw new ConstraintViolation(ConstraintMessage.INTERACTION_RECORD_NO_FOUNDATION);
        }
        throw new ConstraintViolation();
    }

    @Override
    public void delete(String id) throws ConstraintViolation{
        InteractionRecord record = dao.findOne(id);
        updateLastModified(record);
        Foundation f = record.getFoundation();
        updateLastModified(f);
        f.getInteractionRecords().remove(record);
        foundationService.update(f);
    }


    @Override
    public void deleteAll() {
        findAll().stream().forEach(this::uncheckedDelete);
    }

    @Override
    public InteractionRecordType findInteractionRecordType(String id) {
        return typeDao.findOne(id);
    }

    @Override
    public Set<InteractionRecordType> findAllInteractionRecordTypes() {
        return typeDao.findAll();
    }

    @Override
    public String createInteractionRecordType(InteractionRecordType type) {
        typeDao.save(type);
        return type.getId();
    }

    @Override
    public void changeInteractionRecordTypeName(InteractionRecordType type, String name) {
        type.setName(name);
        typeDao.save(type);
    }

    @Override
    public void deleteInteractionRecordType(InteractionRecordType interactionRecordType) {
        Set<InteractionRecord> interactionRecords = dao.findByInteractionType(interactionRecordType);
        for (InteractionRecord record: interactionRecords) {
            record.setInteractionType(null);
        }
        typeDao.delete(interactionRecordType);
    }

    @Override
    public void deleteAllInteractionRecordTypes() {
        typeDao.findAll().stream().forEach(this::deleteInteractionRecordType);
    }
}
