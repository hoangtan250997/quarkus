package com.axonactive.agileskills.base.security.service;

import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.rest.model.JwtRequest;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Base64;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    private UserService userService;

    public boolean checkAuthentication(JwtRequest jwtRequest) throws InputValidationException {
        UserEntity user = userService.getEntityByEmail(jwtRequest.getEmail());
        String password = decryptBase64Password(jwtRequest.getPassword());
        return BCrypt.checkpw(password, user.getPassword());
    }

    private String decryptBase64Password(String password) throws InputValidationException {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(password);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new InputValidationException(ErrorMessage.KEY_PASS_EMAIL_INVALID, ErrorMessage.PASS_EMAIL_INVALID);
        }
    }

}
