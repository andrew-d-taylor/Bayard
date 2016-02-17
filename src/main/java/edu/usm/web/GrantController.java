package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Grant;
import edu.usm.domain.Views;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by andrew on 2/17/16.
 */
@RestController
@RequestMapping("/grants")
public class GrantController {

    @Autowired
    private GrantService grantService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GrantList.class)
    public Set<Grant> getAllGrants() {
        return grantService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GrantDetails.class)
    public Grant getGrant(@PathVariable("id")String id) throws NullDomainReference {
        Grant grant = grantService.findById(id);
        if (null == grant) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullGrant(id);
        }
        return grant;
    }

}
