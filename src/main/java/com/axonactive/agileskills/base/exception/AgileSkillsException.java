package com.axonactive.agileskills.base.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
@ApplicationException
public class AgileSkillsException extends Exception {

    private final transient ResponseBody responseBody;

    public AgileSkillsException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.NOT_FOUND, keyMessage, message);
    }
}
