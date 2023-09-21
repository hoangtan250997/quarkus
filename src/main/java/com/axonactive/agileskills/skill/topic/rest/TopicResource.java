package com.axonactive.agileskills.skill.topic.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.skill.topic.service.TopicService;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
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
@Tag(name = "Topic")
public class TopicResource {

    @Inject
    private TopicService topicService;

    @POST
    @Path("/{skill-id}/topics")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(description = "Create a topic")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Create a topic successfully"
                    , content = @Content(schema = @Schema(implementation = Topic.class))),
            @APIResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Skill not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response create(@RequestBody(
            name = "New topic's info",
            description = "Agile topic to be created",
            required = true
    ) Topic topic, @PathParam("skill-id") Long skillId) throws InputValidationException, ResourceNotFoundException {
        Topic createdTopic = topicService.create(topic, skillId);
        return Response.created(URI.create("topics/" + createdTopic.getId())).entity(createdTopic).build();
    }

    @GET
    @Path("{skill-id}/topics")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "Get active topic list by skill id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active topic list by skill id successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Topic.class))),
            @APIResponse(responseCode = "404", description = "Skill not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getActiveTopicListByActiveSkillId(@Parameter(
            name = "skill-id", required = true
    ) @PathParam("skill-id") Long skillId)
            throws ResourceNotFoundException {
        return Response.ok(topicService.getBySkillIdAndStatus(skillId, StatusEnum.ACTIVE)).build();
    }

    @DELETE
    @Path("/topics/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(description = "Soft delete a topic")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Soft delete a topic"),
            @APIResponse(responseCode = "401", description = "Sign-in required"),
            @APIResponse(responseCode = "403", description = "Unauthorized access"),
            @APIResponse(responseCode = "404", description = "Topic not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response softDelete(@Parameter(
            name = "id",
            description = "Agile topic to be soft deleted",
            required = true
    ) @PathParam("id") Long id) throws ResourceNotFoundException {
        topicService.softDelete(id);
        return Response.noContent().build();
    }
}
