package com.axonactive.agileskills.user.rest;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.utility.JwtUtils;
import com.axonactive.agileskills.user.service.UserService;
import com.axonactive.agileskills.user.service.model.User;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
