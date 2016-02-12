package edu.usm.service.impl;

import edu.usm.domain.Foundation;
import edu.usm.repository.FoundationDao;
import edu.usm.service.FoundationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Set<Foundation> findAll() {
        return (Set<Foundation>) foundationDao.findAll();
    }

    @Override
    public String create(Foundation foundation) {
        foundationDao.save(foundation);
        return foundation.getId();
    }

    @Override
    public void update(Foundation foundation) {
        foundationDao.save(foundation);
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
