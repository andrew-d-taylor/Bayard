package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.FoundationDto;
import edu.usm.dto.Response;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
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

    @Autowired
    private GrantService grantService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createFoundation(@RequestBody Foundation foundation) throws ConstraintViolation{
        String foundationId = foundationService.create(foundation);
        return new Response(foundationId, Response.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response updateFoundationDetails(@PathVariable("id")String id, @RequestBody FoundationDto foundationDto) throws NullDomainReference, ConstraintViolation{
        Foundation fromDb = foundationService.findById(id);
        if (null == fromDb) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullFoundation(id);
        }
        foundationService.update(fromDb, foundationDto);
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
    public Foundation getFoundation(@PathVariable("id") String id)  throws NullDomainReference{
        Foundation foundation = foundationService.findById(id);
        if (null == foundation) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullFoundation(id);
        }
        return foundation;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteFoundation(@PathVariable("id") String id) throws NullDomainReference{
        Foundation foundation = foundationService.findById(id);
        if (null == foundation) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullFoundation(id);
        }
        foundationService.delete(foundation);
        return Response.successGeneric();
    }

    @RequestMapping(value = "/{id}/grants", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createGrant(@PathVariable("id")String id, @RequestBody Grant grant) throws NullDomainReference, ConstraintViolation {
        Foundation foundation = foundationService.findById(id);
        if (null == foundation) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullFoundation(id);
        }
        foundationService.createGrant(foundation, grant);
        return Response.successGeneric();
    }

    @RequestMapping(value = "/{id}/grants/{grantId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteGrant(@PathVariable("id")String id, @PathVariable("grantId")String grantId) throws NullDomainReference, ConstraintViolation {
        Foundation foundation = foundationService.findById(id);
        Grant grant = grantService.findById(grantId);
        if (null == foundation) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullFoundation(id);        }
        if (null == grant) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullGrant(grantId);
        }
        foundationService.deleteGrant(foundation, grant);
        return Response.successGeneric();
    }

}
