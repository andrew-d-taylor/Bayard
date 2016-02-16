package edu.usm.service;

import edu.usm.domain.Foundation;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.FoundationDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by andrew on 2/12/16.
 */
public interface FoundationService {

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Foundation findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Foundation findByName(String name);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Foundation> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Foundation foundation) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Foundation foundation) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Foundation foundation, FoundationDto dto) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(Foundation foundation);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

}
