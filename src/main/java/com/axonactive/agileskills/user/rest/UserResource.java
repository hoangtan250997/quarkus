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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/users")
@Tag(name ="User")
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private JwtUtils jwtUtils;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN"})
    @Operation(summary = "Create a user")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Create a user successfully"
                    , content = @Content(schema = @Schema(implementation = User.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })

    public Response create(@RequestBody(
            name = "New user's info",
            description = "User to be created",
            required = true
    ) User user) throws InputValidationException {
        User createdUser = userService.create(user);
        return Response.created(URI.create("users/" + createdUser.getId())).entity(createdUser).status(Response.Status.CREATED).build();
    }
}
