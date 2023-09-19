package com.axonactive.agileskills.user.rest;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.utility.JwtUtils;
import com.axonactive.agileskills.user.service.UserService;
import com.axonactive.agileskills.user.service.model.User;


import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/users")

public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private JwtUtils jwtUtils;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN"})

    public Response create( User user) throws InputValidationException {
        User createdUser = userService.create(user);
        return Response.created(URI.create("users/" + createdUser.getId())).entity(createdUser).status(Response.Status.CREATED).build();
    }
}
