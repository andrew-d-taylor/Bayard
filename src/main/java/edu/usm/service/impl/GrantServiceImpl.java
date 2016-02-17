package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.repository.FoundationDao;
import edu.usm.repository.GrantDao;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by andrew on 2/16/16.
 */
@Service
public class GrantServiceImpl implements GrantService {

    @Autowired
    private GrantDao grantDao;

    @Autowired
    private FoundationService foundationService;

    @Override
    public void deleteAll() {
        findAll().stream().forEach(this::uncheckedDelete);
    }

    private void uncheckedDelete(Grant grant) {
        try {
            delete(grant);
        } catch (ConstraintViolation e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Grant grant) throws ConstraintViolation {
        Foundation foundation = grant.getFoundation();
        foundation.getGrants().remove(grant);
        foundationService.update(foundation);
    }

    @Override
    public void update(Grant grant) {
        grantDao.save(grant);
    }

    @Override
    public String create(Grant grant) {
        grantDao.save(grant);
        return grant.getId();
    }

    @Override
    public Set<Grant> findAll() {
        return grantDao.findAll();
    }

    @Override
    public Grant findById(String id) {
        return grantDao.findOne(id);
    }
}
