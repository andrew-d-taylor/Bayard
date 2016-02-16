package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Foundation;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.service.FoundationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by andrew on 2/12/16.
 */
@RestController
@RequestMapping("/foundations")
public class FoundationController {

    @Autowired
    private FoundationService foundationService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createFoundation(@RequestBody Foundation foundation) {
        String foundationId = foundationService.create(foundation);
        return new Response(foundationId, Response.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response updateFoundationDetails(@PathVariable("id")String id, @RequestBody Foundation foundation) {
        Foundation fromDb = foundationService.findById(id);
        //TODO: service method to ignore collections when updating
        foundationService.update(foundation);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView({Views.FoundationList.class})
    public Set<Foundation> getAllFoundations() {
        return foundationService.findAll();
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView({Views.FoundationDetails.class})
    public Foundation getFoundation(@PathVariable("id") String id) {
        return foundationService.findById(id);
    }
}
