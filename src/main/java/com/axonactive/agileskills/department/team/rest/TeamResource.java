package com.axonactive.agileskills.department.team.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.team.service.TeamService;
import com.axonactive.agileskills.department.team.service.model.Team;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/departments")
public class TeamResource {

    @Inject
    private TeamService teamService;

    @GET
    @Path("/{department-id}/teams")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get active team list by department id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active team list by department id successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Team.class))),
            @APIResponse(responseCode = "404", description = "Department not found"),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response getActiveListByDepartmentId(@Parameter(
            name = "department-id", required = true
    ) @PathParam("department-id") Long departmentId) throws ResourceNotFoundException {
        return Response.ok(teamService.getByDepartmentIdAndStatus(departmentId, StatusEnum.ACTIVE)).build();
    }

    @GET
    @Path("/teams")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get active team list")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active team list successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Team.class))),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getActiveList() {
        return Response.ok(teamService.getByStatus(StatusEnum.ACTIVE)).build();
    }
}
