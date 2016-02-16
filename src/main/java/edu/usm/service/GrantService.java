package edu.usm.service;

import edu.usm.domain.Grant;

import java.util.Set;

/**
 * Created by andrew on 2/16/16.
 */
public interface GrantService {

    Grant findById(String id);
    Set<Grant> findAll();
    String create(Grant grant);
    void update(Grant grant);
    void delete(Grant grant);
    void deleteAll();

}
