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
import java.net.URI;

@Path("/skills")
public class TopicResource {

    @Inject
    private TopicService topicService;

    @POST
    @Path("/{skill-id}/topics")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})

    public Response create(Topic topic, @PathParam("skill-id") Long skillId) throws InputValidationException, ResourceNotFoundException {
        Topic createdTopic = topicService.create(topic, skillId);
        return Response.created(URI.create("topics/" + createdTopic.getId())).entity(createdTopic).build();
    }

    @GET
    @Path("{skill-id}/topics")
    @Produces({MediaType.APPLICATION_JSON})

    public Response getActiveTopicListByActiveSkillId(@PathParam("skill-id") Long skillId)
            throws ResourceNotFoundException {
        return Response.ok(topicService.getBySkillIdAndStatus(skillId, StatusEnum.ACTIVE)).build();
    }

    @DELETE
    @Path("/topics/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public Response softDelete(@PathParam("id") Long id) throws ResourceNotFoundException {
        topicService.softDelete(id);
        return Response.noContent().build();
    }
}
