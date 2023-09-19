package com.axonactive.agileskills.department.team.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.team.service.TeamService;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/departments")
public class TeamResource {

    @Inject
    private TeamService teamService;

    @GET
    @Path("/{department-id}/teams")
    @Produces({MediaType.APPLICATION_JSON})
    
    public Response getActiveListByDepartmentId( @PathParam("department-id") Long departmentId) throws ResourceNotFoundException {
        return Response.ok(teamService.getByDepartmentIdAndStatus(departmentId, StatusEnum.ACTIVE)).build();
    }

    @GET
    @Path("/teams")
    @Produces({MediaType.APPLICATION_JSON})
    
    public Response getActiveList() {
        return Response.ok(teamService.getByStatus(StatusEnum.ACTIVE)).build();
    }
}
