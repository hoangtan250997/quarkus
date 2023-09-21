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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Tag(name = "Report")
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
    @Operation(summary = "Login")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successfully logged in"
                    , content = @Content(schema =  @Schema(implementation = AuthResource.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getJwtResponse(JwtRequest jwtRequest) throws AuthorizationException, InputValidationException {
        JwtResponse jwtResponse = jwtUtils.generateJwtResponse(jwtRequest);
        return Response.ok(jwtResponse).build();
    }
}
