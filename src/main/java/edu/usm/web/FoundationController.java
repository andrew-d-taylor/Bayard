package edu.usm.web;

import edu.usm.domain.Foundation;
import edu.usm.dto.Response;
import edu.usm.service.FoundationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by andrew on 2/12/16.
 */
@RestController
@RequestMapping("/foundations")
public class FoundationController {

    @Autowired
    private FoundationService foundationService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createFoundation(@RequestBody Foundation foundation) {
        String foundationId = foundationService.create(foundation);
        return new Response(foundationId, Response.SUCCESS);
    }
}
