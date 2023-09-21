package com.axonactive.agileskills.security.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.AuthorizationException;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.security.rest.model.JwtRequest;
import com.axonactive.agileskills.base.security.service.AuthenticationService;
import com.axonactive.agileskills.base.security.service.dto.JwtResponse;
import com.axonactive.agileskills.base.security.utility.JwtUtils;
import com.axonactive.agileskills.user.entity.RoleEnum;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class JwtUtilsTest {

    @InjectMocks
    JwtUtils jwtUtils;

    @Mock
    UserService userService;

    @Mock
    AuthenticationService authenticationService;

    UserEntity user = UserEntity.builder()
            .email("abc123@example.com")
            .password("$2a$10$wn0.ui7k.1qlphsGXVjZ/OlysPVcbeVHqODq5GawhAL7hntjHdOwG")
            .role(RoleEnum.ROLE_USER)
            .status(StatusEnum.ACTIVE)
            .build();
    UserEntity user1 = UserEntity.builder()
            .email("abc123@example.com")
            .password("$2a$10$wn0.ui7k.1qlphsGXVjZ/OlysPVcbeVHqODq5GawhAL7hntjHdOwG")
            .role(RoleEnum.ROLE_USER)
            .status(StatusEnum.INACTIVE)
            .build();
    JwtRequest jwtRequest = JwtRequest.builder()
            .email("abc123@example.com")
            .password("dHVpaGV0bmd1")
            .build();
    JwtRequest jwtRequestWrongEmail = JwtRequest.builder()
            .email("abcd123@example.com")
            .password("dHVpaGV0bmd1")
            .build();
    JwtRequest jwtRequestWrongPass = JwtRequest.builder()
            .email("abcd123@example.com")
            .password("dnVob2FuZzEyMw==")
            .build();

    @Test
    void generateToken_MandatoryFields_ReturnToken() throws AuthorizationException, InputValidationException {
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        assertNotNull(jwtUtils.generateToken(jwtRequest));
    }

    @Test
    void generateToken_InputInvalid_ThrowException() throws InputValidationException, AuthorizationException {
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.FALSE);
        assertThrows(InputValidationException.class, () -> jwtUtils.generateToken(jwtRequest));
    }

    @Test
    void validateJwtToken_TokenIsNull_ThrowException() {
        assertThrows(AuthorizationException.class, () -> jwtUtils.validateJwtToken(null));
    }

    @Test
    void validateJwtToken_TokenIsValid_NotThrowException() throws AuthorizationException, InputValidationException {

        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);
        String token = jwtUtils.generateToken(jwtRequest);
        String verifyToken = "Bearer " + token;

        assertDoesNotThrow(() -> jwtUtils.validateJwtToken(verifyToken));
    }

    @Test
    void validateJwtToken_TokenIsInvalid_ThrowException() {
        Algorithm algorithm = Algorithm.HMAC512("not-secret");
        String token = JWT.create()
                .withIssuer("issuer")
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("EMAIL", jwtRequest.getEmail())
                .withClaim("ROLE", "ROLE_USER")
                .withExpiresAt(new Date(System.currentTimeMillis() + 194135))
                .sign(algorithm);
        assertThrows(AuthorizationException.class, () -> jwtUtils.validateJwtToken(token));
    }

    @Test
    void generateJwtResponse_MandatoryFields_ReturnJwtResponse() throws AuthorizationException, InputValidationException {

        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);

        JwtResponse actualResponse = jwtUtils.generateJwtResponse(jwtRequest);

        assertEquals(user.getEmail(), actualResponse.getEmail());
        assertEquals(user.getRole(), actualResponse.getRole());

    }

    @Test
    void generateJwtResponse_StatusInactive_ThrowException() throws InputValidationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user1);
        assertThrows(AuthorizationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void generateJwtResponse_EmailIsWrongFormat_ThrowException() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email("abc")
                .password("dHVpaGV0bmd1")
                .build();
        assertThrows(ConstraintViolationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void generateJwtResponse_EmailIsNull_ThrowException() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(null)
                .password("dHVpaGV0bmd1")
                .build();
        assertThrows(ConstraintViolationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void generateJwtResponse_EmailIsBlank_ThrowException() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(" ")
                .password("dHVpaGV0bmd1")
                .build();
        assertThrows(ConstraintViolationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void generateJwtResponse_PasswordIsBlank_ThrowException() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email("abc123@email.com")
                .password(" ")
                .build();
        assertThrows(ConstraintViolationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void generateJwtResponse_PasswordIsNull_ThrowException() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email("abc123@email.com")
                .password(null)
                .build();
        assertThrows(ConstraintViolationException.class, () -> jwtUtils.generateJwtResponse(jwtRequest));
    }

    @Test
    void getRoleFromToken_ValidToken_ReturnRoleEnum() throws AuthorizationException, InputValidationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);

        String token = jwtUtils.generateToken(jwtRequest);
        String verifyToken = "Bearer " + token;
        RoleEnum role = jwtUtils.getRoleFromToken(verifyToken);
        assertEquals(user.getRole(), role);
    }

    @Test
    void getEmailFromToken_validToken_ReturnEmail() throws AuthorizationException, InputValidationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);

        String token = jwtUtils.generateToken(jwtRequest);
        String verifyToken = "Bearer " + token;
        String email = jwtUtils.getEmailFromToken(verifyToken);
        assertEquals(user.getEmail(), email);
    }

    @Test
    void getExpireTimeFromToken_ValidToken_ReturnMillisecond() throws AuthorizationException, InputValidationException {
        when(userService.getEntityByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(authenticationService.checkAuthentication(jwtRequest)).thenReturn(Boolean.TRUE);

        String token = jwtUtils.generateToken(jwtRequest);
        String verifyToken = "Bearer " + token;
        Date expire = jwtUtils.getExpireTokenTime(verifyToken);
        assertNotEquals(new Date(System.currentTimeMillis()), expire);
    }
}
