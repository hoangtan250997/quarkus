package com.axonactive.agileskills.base.security.rest;

import com.axonactive.agileskills.base.exception.AuthorizationException;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.rest.model.JwtRequest;
import com.axonactive.agileskills.base.security.service.AuthenticationService;
import com.axonactive.agileskills.base.security.service.dto.JwtResponse;
import com.axonactive.agileskills.base.security.utility.JwtUtils;
import com.axonactive.agileskills.user.service.UserService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})


    public Response getJwtResponse(JwtRequest jwtRequest) throws AuthorizationException, InputValidationException {
        JwtResponse jwtResponse = jwtUtils.generateJwtResponse(jwtRequest);
        return Response.ok(jwtResponse).build();
    }
}
