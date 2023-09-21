package com.axonactive.agileskills.base.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class AgileSkillsExceptionMapper implements ExceptionMapper<AgileSkillsException> {
    private static final Logger logger = LogManager.getLogger(AgileSkillsExceptionMapper.class);

    @Override
    public Response toResponse(AgileSkillsException e) {
        StackTraceElement[] stackTraceArray = e.getStackTrace();
        for (StackTraceElement s : stackTraceArray) {
            logger.error(s.getClassName());
        }
        ResponseBody responseBody = e.getResponseBody();

        return Response.status(responseBody.getStatusCode())
                .entity(responseBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
