package com.axonactive.agileskills.position.rest;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.service.PositionService;
import com.axonactive.agileskills.position.service.mapper.PositionMapper;
import com.axonactive.agileskills.position.service.model.Position;


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
import java.net.URI;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_NULL_SEARCH_WORD;

@Path("/positions")
public class PositionResource {

    @Inject
    private PositionService positionService;

    @Inject
    private PositionMapper positionMapper;

    @GET
    @Path("/{id}/filtered")
    @Produces({MediaType.APPLICATION_JSON})
    public Response displayPositionWithRequiredSkillAndRequiredTopic(@PathParam("id") Long id) throws ResourceNotFoundException {
        PositionEntity displayedPositionEntity = positionService.displayPositionWithRequiredSkillAndRequiredTopic(id);
        return Response.ok(positionMapper.toDTO(displayedPositionEntity)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getOpenList() {
        return Response.ok(positionService.getByStatus(PositionStatusEnum.OPEN)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response close(@PathParam("id") Long id) throws ResourceNotFoundException {
        positionService.close(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getOpenPositionById(@PathParam("id") Long id) throws ResourceNotFoundException {
        return Response.ok(positionService.getByIdAndStatus(id, PositionStatusEnum.OPEN)).build();
    }

    @GET
    @Path("/years")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByYear() {
        return Response.ok(positionService.getYears()).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createPositionWithRequiredSkill(Position position)
            throws ResourceNotFoundException, InputValidationException {
        Position newPosition = positionService.createPositionWithRequiredSkill(position);
        return Response.created(URI.create("/positions" + newPosition.getId())).entity(newPosition).build();
    }

    @GET
    @Path("/search")
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchStatusOpenByWords(@QueryParam("word") @NotNull(message = KEY_NULL_SEARCH_WORD) String word) {
        return Response.ok(positionService.searchOpenPositionsByWord(word)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Long id, Position position) throws InputValidationException, ResourceNotFoundException {
        PositionEntity updatePositionEntity = positionService.update(id, position);
        Position updatePosition = positionMapper.toDTO(updatePositionEntity);
        return Response.ok(updatePosition).build();
    }
}
