package com.axonactive.agileskills.base.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<InputValidationException> {

    private static final Logger logger = LogManager.getLogger(InputValidationExceptionMapper.class);

    @Override
    public Response toResponse(InputValidationException e) {
        StackTraceElement[] stackTraceArray = e.getStackTrace();
        String logMessage = String.format("%s:%d - %s",
                stackTraceArray[0].getClassName(),
                stackTraceArray[0].getLineNumber(),
                e.getResponseBody().getErrorMessage());
        logger.info(logMessage);
        ResponseBody responseBody = e.getResponseBody();

        return Response.status(responseBody.getStatusCode())
                .entity(responseBody)
                .type(MediaType.APPLICATION_JSON)
                .build();

    }
}