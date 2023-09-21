package com.axonactive.agileskills.department.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.service.DepartmentService;
import com.axonactive.agileskills.department.service.model.Department;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/departments")
@Tag(name = "Department")
public class DepartmentResource {

    @Inject
    private DepartmentService departmentService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get active department list")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get active department list successfully"
                    , content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Department.class))
            ),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getActiveList() {
        return Response.ok(departmentService.getByStatus(StatusEnum.ACTIVE)).build();
    }
}