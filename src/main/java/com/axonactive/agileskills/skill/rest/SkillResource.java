package com.axonactive.agileskills.skill.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.skill.service.SkillService;
import com.axonactive.agileskills.skill.service.model.Skill;

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
public class SkillResource {

    @Inject
    private SkillService skillService;
    
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getActiveById(@PathParam("id") Long skillId) throws ResourceNotFoundException {
        return Response.ok(skillService.getByIdAndStatus(skillId, StatusEnum.ACTIVE)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getActiveList() {
        return Response.ok(skillService.getByStatus(StatusEnum.ACTIVE)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Skill skill) throws InputValidationException {
        Skill createdSkill = skillService.create(skill);
        return Response.created(URI.create("skills/" + createdSkill.getId())).entity(createdSkill).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response softDelete(@PathParam("id") Long id) throws ResourceNotFoundException {
        skillService.softDelete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/skills-with-topics")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSkillListIncludingTopicList() throws ResourceNotFoundException {
        return Response.ok(skillService.getSkillListIncludingTopicList()).build();
    }
}
