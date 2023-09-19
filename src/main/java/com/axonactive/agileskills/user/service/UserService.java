package com.axonactive.agileskills.user.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.user.dao.UserDAO;
import com.axonactive.agileskills.user.entity.RoleEnum;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.mapper.UserMapper;
import com.axonactive.agileskills.user.service.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.MessageInterpolator;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.mindrot.jbcrypt.BCrypt;


import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Base64;
import java.util.Set;

@ApplicationScoped
public class UserService {

    private static final Validator validator =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator((MessageInterpolator) new ParameterMessageInterpolator())
                    .buildValidatorFactory()
                    .getValidator();
    @Inject
    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    public User create(User user) throws InputValidationException, IllegalArgumentException {
        verifyUser(user);

        UserEntity userEntity = UserEntity.builder()
                .name(user.getName().trim())
                .email(user.getEmail())
                .password(BCrypt.hashpw(decodePassword(user.getPassword()), BCrypt.gensalt()))
                .status(StatusEnum.ACTIVE)
                .role(RoleEnum.ROLE_USER)
                .build();
        return userMapper.toDTO(userDAO.create(userEntity));
    }

    public UserEntity getEntityByEmail(String email) throws InputValidationException {
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new InputValidationException(ErrorMessage.KEY_PASS_EMAIL_INVALID, ErrorMessage.PASS_EMAIL_INVALID));
    }

    public void verifyUser(User user) throws InputValidationException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new ConstraintViolationException(violations);
        }
        if (isUserExisted(user.getEmail())) {
            throw new InputValidationException(ErrorMessage.KEY_USER_ALREADY_EXISTED,
                    ErrorMessage.USER_ALREADY_EXISTED);
        }
        if (isNameNullOrEmpty(user.getName())) {
            user.setName(user.getEmail().split("@")[0]);
        }
        if (!isPasswordMatchPattern(user.getPassword())) {
            throw new InputValidationException(ErrorMessage.KEY_PASSWORD_NOT_MATCH_PATTERN,
                    ErrorMessage.PASSWORD_NOT_MATCH_PATTERN);
        }
    }

    private boolean isNameNullOrEmpty(String name) {
        return name == null || name.trim().equals("");
    }

    private boolean isUserExisted(String email) {
        return userDAO.findByEmail(email.trim().toLowerCase()).isPresent();
    }

    private boolean isPasswordMatchPattern(String password) throws InputValidationException {
        String pattern = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,}$";
        return decodePassword(password).matches(pattern);
    }

    private String decodePassword(String password) throws InputValidationException {
        try {
            byte[] decoded = Base64.getDecoder().decode(password);
            return (new String(decoded));
        } catch (IllegalArgumentException e) {
            throw new InputValidationException(ErrorMessage.KEY_PASSWORD_NOT_ENCODED,
                    ErrorMessage.PASSWORD_NOT_ENCODED);
        }
    }
}
