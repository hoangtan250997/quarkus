package com.axonactive.agileskills.user.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.user.dao.UserDAO;
import com.axonactive.agileskills.user.entity.RoleEnum;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.mapper.UserMapper;
import com.axonactive.agileskills.user.service.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    UserEntity user = UserEntity.builder()
            .name("huy")
            .email("huy.nguyen@gmail.com")
            .status(StatusEnum.ACTIVE)
            .password("$2a$10$xdaloG0Rk.SH3fCa9X.D3OBeDbPauBT7NTI7YxoiRznK0kKmRTIuC")
            .build();
    User userDTO = User.builder()
            .name("huy")
            .email("huy.nguyen@gmail.com")
            .status(StatusEnum.ACTIVE)
            .build();
    @InjectMocks
    private UserService userService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private UserMapper userMapper;

    @Test
    void createUser_NameLessThan255Chars_ValidPasswordHas6Chars_ValidEmail_ReturnModelWithNoPassword() throws InputValidationException {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Binh")
                .email("binh.200@gmail.com")
                .password("hello1")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User userDTO = User.builder()
                .id(1L)
                .name("Binh")
                .email("binh.200@gmail.com")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name("Binh")
                .email("binh.200@gmail.com")
                .password("aGVsbG8x")
                .build();

        when(userDAO.create(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        User result = userService.create(createdUser);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(StatusEnum.ACTIVE, result.getStatus());
        assertEquals(RoleEnum.ROLE_USER, result.getRole());
    }

    @Test
    void createUser_NameHas255Chars_ValidPasswordMoreThan6Chars_ValidEmail_ReturnModelWithNoPassword() throws InputValidationException {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("B".repeat(255))
                .email("binh200@gmail.com")
                .password("hello123")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User userDTO = User.builder()
                .id(1L)
                .name("B".repeat(255))
                .email("binh200@gmail.com")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name("B".repeat(255))
                .email("binh200@gmail.com")
                .password("aGVsbG8xMjM=")
                .build();

        when(userDAO.create(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        User result = userService.create(createdUser);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(StatusEnum.ACTIVE, result.getStatus());
        assertEquals(RoleEnum.ROLE_USER, result.getRole());
    }

    @Test
    void createUser_NameIsNull_ReturnNameEqualsEmailBeforeAt() throws InputValidationException {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("binh200")
                .email("binh200@gmail.com")
                .password("hello123")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User userDTO = User.builder()
                .id(1L)
                .name("binh200@gmail.com".split("@")[0])
                .email("binh200@gmail.com")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name(null)
                .email("binh200@gmail.com")
                .password("aGVsbG8xMjM=")
                .build();

        when(userDAO.create(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        User result = userService.create(createdUser);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(StatusEnum.ACTIVE, result.getStatus());
        assertEquals(RoleEnum.ROLE_USER, result.getRole());
    }

    @Test
    void createUser_NameIsEmpty_ReturnNameEqualsEmailBeforeAt() throws InputValidationException {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("binh200")
                .email("binh200@gmail.com")
                .password("hello123")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User userDTO = User.builder()
                .id(1L)
                .name("binh200@gmail.com".split("@")[0])
                .email("binh200@gmail.com")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name("")
                .email("binh200@gmail.com")
                .password("aGVsbG8xMjM=")
                .build();

        when(userDAO.create(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        User result = userService.create(createdUser);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(StatusEnum.ACTIVE, result.getStatus());
        assertEquals(RoleEnum.ROLE_USER, result.getRole());
    }

    @Test
    void createUser_EmailLocalPart64CharsAndDomainPart63Chars_ReturnModelWithNoPassword() throws InputValidationException {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Binh")
                .email("a".repeat(64) + "@" + "b".repeat(63))
                .password("hello123")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User userDTO = User.builder()
                .id(1L)
                .name("Binh")
                .email("a".repeat(64) + "@" + "b".repeat(63))
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name("Binh")
                .email("a".repeat(64) + "@" + "b".repeat(63))
                .password("aGVsbG8xMjM=")
                .build();

        when(userDAO.create(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        User result = userService.create(createdUser);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(StatusEnum.ACTIVE, result.getStatus());
        assertEquals(RoleEnum.ROLE_USER, result.getRole());
    }

    @Test
    void createUser_NameOver255Chars_ThrowException() {
        User createdUser = User.builder()
                .name("a".repeat(256))
                .email("binh200@gmail.com")
                .password(" ")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(createdUser));
    }

    @Test
    void createUser_EmailIsNull_ThrowException() {
        User nullEmailUser = getUser(null);
        User emptyEmailUser = getUser("");
        User wrongFormatEmailUser = getUser("notAValidEmailFormat");
        User moreThanOneAtUser = getUser("not@A@ValidEmailFormat@.com");
        User invalidSpecialCharLocalPartEmailuser = getUser(";Binh200@.com");
        User emailLocalPartContainQuoteUser = getUser("just\"not\"right@example.com");
        User emailLocalPartExceed64CharsUser = getUser("a".repeat(65) + "@axonactive.com");
        User emailDomainPartExceeds63CharsUser = getUser("daohoabinh@" + "a".repeat(64) + ".com");

        Arrays.asList(nullEmailUser, emptyEmailUser, wrongFormatEmailUser, moreThanOneAtUser,
                        invalidSpecialCharLocalPartEmailuser, emailLocalPartContainQuoteUser, emailLocalPartExceed64CharsUser,
                        emailDomainPartExceeds63CharsUser)
                .forEach(user -> assertThrows(ConstraintViolationException.class, () -> userService.create(user), ErrorMessage.EMAIL_BLANK_OR_NULL));
    }

    @Test
    void createUser_DuplicateEmail_ThrowException() {
        UserEntity user = UserEntity.builder()
                .name("binh200")
                .email("binh200@gmail.com")
                .password("aGVsbG8xMjM=")
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();

        User createdUser = User.builder()
                .name(null)
                .email("binh200@gmail.com")
                .password("aGVsbG8xMjM=")
                .build();

        when(userDAO.findByEmail(createdUser.getEmail().trim().toLowerCase()))
                .thenReturn(Optional.ofNullable(user));

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.USER_ALREADY_EXISTED);
    }

    @Test
    void createUser_PasswordNotMatchRegex_ThrowException() {
        User createdUser = User.builder()
                .name("Binh")
                .email("binh200@gmail.com")
                .password("aGVsMQ==")
                .build();

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_NOT_MATCH_PATTERN);
    }

    @Test
    void createUser_PasswordNotEncoded_ThrowException() {
        User createdUser = User.builder()
                .name("Binh")
                .email("binh200@gmail.com")
                .password("a")
                .build();

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_NOT_ENCODED);
    }

    @Test
    void createUser_PasswordSHA256Encoded_ThrowException() {
        User createdUser = User.builder()
                .name("Binh")
                .email("binh200@gmail.com")
                .password("6D141E0503F9BEC5560AC88F690B9A01AC975FCF6F83FD5E38088DE08627EAA5")
                .build();

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_NOT_ENCODED);
    }

    @Test
    void createUser_PasswordIsNull_ThrowException() {
        User createdUser = User.builder()
                .email("binh200@gmail.com")
                .password(null)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_BLANK_OR_NULL);
    }

    @Test
    void createUser_PasswordIsBlank_ThrowException() {
        User createdUser = User.builder()
                .email("binh200@gmail.com")
                .password(" ")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_BLANK_OR_NULL);
    }

    @Test
    void createUser_PasswordLessThan6Chars_ThrowException() {
        User createdUser = User.builder()
                .email("binh200@gmail.com")
                .password("YWJjZGU=")
                .build();

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_NOT_MATCH_PATTERN);
    }

    @Test
    void createUser_PasswordNotHave1Number_ThrowException() {
        User createdUser = User.builder()
                .email("binh200@gmail.com")
                .password("YWJjZGVm")
                .build();

        assertThrows(InputValidationException.class, () -> userService.create(createdUser),
                ErrorMessage.PASSWORD_NOT_MATCH_PATTERN);
    }

    @Test
    void validateUser_EmailIsFound_ReturnCompatibleUser() throws InputValidationException {
        when(userDAO.findByEmail(userDTO.getEmail())).thenReturn(Optional.ofNullable(user));
        assertEquals(user.getEmail(), userService.getEntityByEmail(userDTO.getEmail()).getEmail());
        assertEquals(user.getName(), userService.getEntityByEmail(userDTO.getEmail()).getName());
        assertEquals(user.getRole(), userService.getEntityByEmail(userDTO.getEmail()).getRole());
        assertEquals(user.getStatus(), userService.getEntityByEmail(userDTO.getEmail()).getStatus());
    }

    @Test
    void validateUser_EmailNotFound_ThrowException() {
        when(userDAO.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        assertThrows(InputValidationException.class, () -> userService.getEntityByEmail(userDTO.getEmail()));
    }

    private User getUser(String email) {
        return User.builder()
                .email(email)
                .password("aGVsbG8xMjM=")
                .build();
    }
}