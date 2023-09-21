package com.axonactive.agileskills.base.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    private static final Logger logger = LogManager.getLogger(JsonMappingExceptionMapper.class);

    @Override
    public Response toResponse(JsonMappingException e) {
        String errorKey = "exception.input.validation.json.format.invalid";
        String errorMessage = "Json format is invalid";

        if (e.getPathReference().contains("openedDate")) {
            errorKey = "exception.input.validation.date.format.invalid";
            errorMessage = "Date format is invalid. Please use ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss.SSSSSSxxx)";
        }
        ResponseBody responseBody = new ResponseBody(Response.Status.BAD_REQUEST, errorKey, errorMessage);
        StackTraceElement[] stackTraceArray = e.getStackTrace();
        String logMessage = String.format("%s:%d - %s",
                stackTraceArray[0].getClassName(),
                stackTraceArray[0].getLineNumber(),
                responseBody.getErrorMessage());
        logger.info(logMessage);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBody)
                .build();
    }
}
