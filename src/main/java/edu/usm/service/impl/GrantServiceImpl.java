package edu.usm.service.impl;

import edu.usm.domain.Grant;
import edu.usm.repository.GrantDao;
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

    @Override
    public void deleteAll() {
        findAll().stream().forEach(this::delete);
    }

    @Override
    public void delete(Grant grant) {
        grantDao.delete(grant);
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
