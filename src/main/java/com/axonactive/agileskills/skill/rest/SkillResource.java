package com.axonactive.agileskills.skill.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.position.service.model.Position;
import com.axonactive.agileskills.skill.service.SkillService;
import com.axonactive.agileskills.skill.service.model.Skill;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/skills")
@Tag(name = "Skill")
public class SkillResource {

    @Inject
    private SkillService skillService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get active skill by id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active skill by id successfully"
                    , content = @Content(schema = @Schema(implementation = Skill.class))),
            @APIResponse(responseCode = "404", description = "Skill not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getActiveById(@PathParam("id") Long skillId) throws ResourceNotFoundException {
        return Response.ok(skillService.getByIdAndStatus(skillId, StatusEnum.ACTIVE)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get active skill list, paged")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active skill list successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Skill.class))),
            @APIResponse(responseCode = "400", description = "Parameters invalid - page or size less than one"),
            @APIResponse(responseCode = "404", description = "Page out of range"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getActiveList() {
        return Response.ok(skillService.getByStatus(StatusEnum.ACTIVE)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a skill")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Create skill successfully"
                    , content = @Content(schema = @Schema(implementation = Position.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response create(@RequestBody(
            name = "New skill's info",
            description = "Agile skill to be created",
            required = true
    ) Skill skill) throws InputValidationException {
        Skill createdSkill = skillService.create(skill);
        return Response.created(URI.create("skills/" + createdSkill.getId())).entity(createdSkill).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Soft delete a skill")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Soft delete a skill"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Skill not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })

    public Response softDelete(@Parameter(
            in = ParameterIn.PATH,
            description = "Agile skill to be soft deleted",
            required = true
    ) @PathParam("id") Long id) throws ResourceNotFoundException {
        skillService.softDelete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/skills-with-topics")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get all skills including topics")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get all skills including topics"
                    , content = @Content(schema = @Schema(implementation = Skill.class))),
            @APIResponse(responseCode = "404", description = "Resource not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getSkillListIncludingTopicList() throws ResourceNotFoundException {
        return Response.ok(skillService.getSkillListIncludingTopicList()).build();
    }
}
