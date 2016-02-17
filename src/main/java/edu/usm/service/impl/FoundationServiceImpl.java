package edu.usm.service.impl;

import edu.usm.domain.Foundation;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.FoundationDto;
import edu.usm.repository.FoundationDao;
import edu.usm.service.FoundationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Set;

/**
 * Created by andrew on 2/12/16.
 */
@Service
public class FoundationServiceImpl implements FoundationService {

    @Autowired
    private FoundationDao foundationDao;

    @Override
    public Foundation findById(String id) {
        return foundationDao.findOne(id);
    }

    @Override
    public Foundation findByName(String name) {
        return foundationDao.findByName(name);
    }

    @Override
    public Set<Foundation> findAll() {
        return (Set<Foundation>) foundationDao.findAll();
    }

    @Override
    public String create(Foundation foundation) throws ConstraintViolation {
        try {
            foundationDao.save(foundation);
        } catch (DataAccessException | TransactionSystemException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(foundation);
        }
        return foundation.getId();
    }

    private void handlePersistenceException(Foundation foundation) throws ConstraintViolation{
        if (null == foundation.getName()) {
            throw new ConstraintViolation(ConstraintMessage.FOUNDATION_REQUIRED_NAME);
        }
        Foundation existing = findByName(foundation.getName());
        if (null != existing) {
            throw new ConstraintViolation.NonUniqueDomainEntity(ConstraintMessage.FOUNDATION_DUPLICATE_NAME, existing);
        }
        throw new ConstraintViolation();
    }

    @Override
    public void update(Foundation foundation) throws ConstraintViolation{
        try {
            foundationDao.save(foundation);
        } catch (DataAccessException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(foundation);
        }
    }

    @Override
    public void update(Foundation foundation, FoundationDto dto) throws ConstraintViolation{
        foundation.setName(dto.getName());
        foundation.setAddress(dto.getAddress());
        foundation.setWebsite(dto.getWebsite());
        foundation.setCurrentGrantor(dto.isCurrentGrantor());
        foundation.setPrimaryContactName(dto.getPrimaryContactName());
        foundation.setPrimaryContactTitle(dto.getPrimaryContactTitle());
        foundation.setPrimaryContactPhone(dto.getPrimaryContactPhone());
        foundation.setPrimaryContactEmail(dto.getPrimaryContactEmail());
        foundation.setSecondaryContactName(dto.getSecondaryContactName());
        foundation.setSecondaryContactTitle(dto.getSecondaryContactTitle());
        foundation.setSecondaryContactPhone(dto.getSecondaryContactPhone());
        foundation.setSecondaryContactEmail(dto.getSecondaryContactEmail());

        update(foundation);
    }

    @Override
    public void delete(Foundation foundation) {
        foundationDao.delete(foundation);
    }

    @Override
    public void deleteAll() {
        Set<Foundation> foundations = findAll();
        foundations.stream().forEach(this::delete);
    }
}
