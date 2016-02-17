package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Grant;
import edu.usm.domain.Views;
import edu.usm.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

}
