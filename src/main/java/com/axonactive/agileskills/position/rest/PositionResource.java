package com.axonactive.agileskills.position.rest;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.service.PositionService;
import com.axonactive.agileskills.position.service.mapper.PositionMapper;
import com.axonactive.agileskills.position.service.model.Position;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_NULL_SEARCH_WORD;

@Path("/positions")
@Tag(name = "Position")
public class PositionResource {

    @Inject
    private PositionService positionService;

    @Inject
    private PositionMapper positionMapper;

    @GET
    @Path("/{id}/filtered")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get filtered open position by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Get filtered open position by id successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "400", description = "Position not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response displayPositionWithRequiredSkillAndRequiredTopic(@PathParam("id") Long id) throws ResourceNotFoundException {
        PositionEntity displayedPositionEntity = positionService.displayPositionWithRequiredSkillAndRequiredTopic(id);
        return Response.ok(positionMapper.toDTO(displayedPositionEntity)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get open position list by status open")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get open position list by status open successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Position.class))),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response getOpenList() {
        return Response.ok(positionService.getByStatus(PositionStatusEnum.OPEN)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Close a position")
    @SecurityScheme(
            securitySchemeName = "bearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Position closed"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Position not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response close(@Parameter(
            in = ParameterIn.PATH,
            name = "id",
            description = "Id of the position to be closed",
            required = true
    ) @PathParam("id") Long id) throws ResourceNotFoundException {
        positionService.close(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get open position by id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get open position by id successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "404", description = "Position not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })

    public Response getOpenPositionById(@PathParam("id") Long id) throws ResourceNotFoundException {
        return Response.ok(positionService.getByIdAndStatus(id, PositionStatusEnum.OPEN)).build();
    }

    @GET
    @Path("/years")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get open position years")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get open position years successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })

    public Response getByYear() {
        return Response.ok(positionService.getYears()).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Create new position with required skill and required topic")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Create position successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Team not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response createPositionWithRequiredSkill(Position position)
            throws ResourceNotFoundException, InputValidationException {
        Position newPosition = positionService.createPositionWithRequiredSkill(position);
        return Response.created(URI.create("/positions" + newPosition.getId())).entity(newPosition).build();
    }

    @GET
    @Path("/search")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Search open position by words")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Search open position by words successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response searchStatusOpenByWords(@QueryParam("word") @NotNull(message = KEY_NULL_SEARCH_WORD) String word) {
        return Response.ok(positionService.searchOpenPositionsByWord(word)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(description = "Update position with required skill and required topic")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Update position successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Position/Team not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response update(@PathParam("id") Long id, Position position) throws InputValidationException, ResourceNotFoundException {
        PositionEntity updatePositionEntity = positionService.update(id, position);
        Position updatePosition = positionMapper.toDTO(updatePositionEntity);
        return Response.ok(updatePosition).build();
    }
}
