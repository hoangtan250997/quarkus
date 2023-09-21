package com.axonactive.agileskills.security.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.AuthorizationException;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.rest.model.JwtRequest;
import com.axonactive.agileskills.base.security.service.AuthenticationService;
import com.axonactive.agileskills.user.entity.RoleEnum;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    UserService userService;

    UserEntity user = UserEntity.builder()
            .email("abc123@example.com")
            .password("$2a$10$wn0.ui7k.1qlphsGXVjZ/OlysPVcbeVHqODq5GawhAL7hntjHdOwG")
            .role(RoleEnum.ROLE_USER)
            .status(StatusEnum.ACTIVE)
            .build();
    JwtRequest jwtRequest = JwtRequest.builder()
            .email("abc123@example.com")
            .password("dHVpaGV0bmd1")
            .build();
    JwtRequest jwtRequestWrongPassword = JwtRequest.builder()
            .email("abc123@example.com")
            .password("dnVob2FuZzEyMw==")
            .build();
    JwtRequest jwtRequestWrongPassFormat = JwtRequest.builder()
            .email("abc123@example.com")
            .password("dnVob2FuZzEyMw====")
            .build();

    @Test
    void checkAuthentication_CorrectEmailAndPassword_ReturnTrue() throws InputValidationException, AuthorizationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        assertTrue(authenticationService.checkAuthentication(jwtRequest));
    }

    @Test
    void checkAuthentication_IncorrectPassword_ReturnFalse() throws InputValidationException, AuthorizationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        assertFalse(authenticationService.checkAuthentication(jwtRequestWrongPassword));
    }

    @Test
    void checkAuthentication_NonExistedUser_ThrowException() throws InputValidationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenThrow(new InputValidationException(ErrorMessage.KEY_PASS_EMAIL_INVALID, ErrorMessage.PASS_EMAIL_INVALID));
        assertThrows(InputValidationException.class,
                () -> authenticationService.checkAuthentication(jwtRequest));
    }

    @Test
    void checkAuthentication_WrongFormatPassword_ThrowException() throws InputValidationException {
        when(userService.getEntityByEmail(jwtRequestWrongPassFormat.getEmail())).thenReturn(user);
        assertThrows(InputValidationException.class,
                () -> authenticationService.checkAuthentication(jwtRequestWrongPassFormat));
    }
}