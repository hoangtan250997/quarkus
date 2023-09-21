package com.axonactive.agileskills.report.rest;

import com.axonactive.agileskills.report.service.StatisticService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jdk.javadoc.doclet.Reporter;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/statistics")
@Tag(name = "Statistic Report")
public class StatisticReport {
    @Inject
    private StatisticService statisticService;

    @GET
    @Path("/{year}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get open statistic by year")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Get open statistic by year successfully"
                    , content = @Content(schema = @Schema(implementation = Reporter.class))),
            @APIResponse(responseCode = "500", description = "Request cannot be fulfilled through browser due to server-side problems"),
    })
    public Response getStatisticByYear(@PathParam("year") Integer year) {
        return Response.ok(statisticService.getStatisticByYear(year)).build();
    }
}
