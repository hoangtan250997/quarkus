package com.axonactive.agileskills.department.rest;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.service.DepartmentService;
import jakarta.inject.Inject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/departments")
public class DepartmentResource {

    @Inject
    private DepartmentService departmentService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
      public Response getActiveList() {
        return Response.ok(departmentService.getByStatus(StatusEnum.ACTIVE)).build();
    }
}