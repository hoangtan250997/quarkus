package com.axonactive.agileskills.base.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {

    //SKILL
    public static final String SKILL_NOT_FOUND = "Skill not found";
    public static final String KEY_SKILL_NOT_FOUND = "exception.resource.not.found.skill";
    public static final String SKILL_ALREADY_EXISTED = "Skill already existed";
    public static final String KEY_SKILL_ALREADY_EXISTED = "exception.input.validation.skill.already.existed";
    public static final String SKILL_NAME_LENGTH_CONSTRAINT = "Skill Name length cannot exceed 255 characters";
    public static final String KEY_SKILL_NAME_LENGTH_CONSTRAINT = "exception.input.validation.skill.name.length.over.max.length";
    public static final String SKILL_DESCRIPTION_LENGTH_CONSTRAINT = "Skill Description length cannot exceed 2000 characters";
    public static final String KEY_SKILL_DESCRIPTION_LENGTH_CONSTRAINT = "exception.input.validation.skill.description.over.max.length";
    public static final String SKILL_NAME_NULL_OR_BLANK = "Skill Name cannot be null or blank";
    public static final String KEY_SKILL_NAME_NULL_OR_BLANK = "exception.input.validation.skill.name.blank.or.null";

    //DATE
    public static final String DATE_INVALID = "Date format is invalid";
    public static final String KEY_DATE_INVALID = "exception.input.validation.date.format.invalid";

    //TOPIC
    public static final String TOPIC_NOT_FOUND = "Topic not found";
    public static final String KEY_TOPIC_NOT_FOUND = "exception.resource.not.found.topic";
    public static final String DUPLICATED_TOPIC_NAME = "Topic Name duplicated";
    public static final String KEY_DUPLICATED_TOPIC_NAME = "exception.input.validation.duplicated.topic.name";
    public static final String TOPIC_NAME_BLANK_OR_NULL = "Topic Name cannot be null or blank";
    public static final String KEY_TOPIC_NAME_BLANK_OR_NULL = "exception.input.validation.topic.name.null.or.blank";
    public static final String TOPIC_NAME_LENGTH_CONSTRAINT = "Topic Name must have at least 3 characters and less than 255 characters";
    public static final String KEY_TOPIC_NAME_LENGTH_CONSTRAINT = "exception.input.validation.topic.name.length.invalid";
    public static final String TOPIC_DESCRIPTION_LENGTH_CONSTRAINT = "Topic Description length cannot exceed 2000 characters";
    public static final String KEY_TOPIC_DESCRIPTION_LENGTH_CONSTRAINT = "exception.input.validation.topic.description.over.max.length";

    //USER
    public static final String KEY_USER_ALREADY_EXISTED = "exception.input.validation.user.already.existed";
    public static final String USER_ALREADY_EXISTED = "User already existed";
    public static final String USER_NAME_LENGTH_CONSTRAINT = "User Name cannot exceed 255 characters";
    public static final String KEY_USER_NAME_LENGTH_CONSTRAINT = "exception.input.validation.user.name.over.max.length";
    public static final String EMAIL_BLANK_OR_NULL = "Email cannot be blank or null";
    public static final String KEY_EMAIL_BLANK_OR_NULL = "exception.input.validation.email.blank.or.null";
    public static final String PASSWORD_BLANK_OR_NULL = "Password cannot be blank or null";
    public static final String KEY_PASSWORD_BLANK_OR_NULL = "exception.input.validation.password.blank.or.null";
    public static final String EMAIL_WRONG_FORMAT = "Email is not the right format";
    public static final String KEY_EMAIL_WRONG_FORMAT = "exception.input.validation.email.wrong.format";
    public static final String PASS_EMAIL_INVALID = "Email or Password is wrong.";
    public static final String KEY_PASS_EMAIL_INVALID = "exception.input.validation.email.password.wrong";

    //Authorization
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access";
    public static final String KEY_UNAUTHORIZED_ACCESS = "exception.security.unauthorized.access";
    public static final String FORBIDDEN_ACCESS = "Forbidden Access";
    public static final String KEY_FORBIDDEN_ACCESS = "exception.security.forbidden.access";
    public static final String PASSWORD_NOT_MATCH_PATTERN = "Password must be at least 6 characters and at least 1 number";
    public static final String KEY_PASSWORD_NOT_MATCH_PATTERN = "exception.input.validation.password.not.match.pattern";
    public static final String PASSWORD_NOT_ENCODED = "Password is not encoded";
    public static final String KEY_PASSWORD_NOT_ENCODED = "exception.input.validation.password.not.encoded";

    //Department
    public static final String DEPARTMENT_NOT_FOUND = "Department not found";
    public static final String KEY_DEPARTMENT_NOT_FOUND = "exception.resource.not.found.department";
    public static final String DEPARTMENT_NAME_NULL_OR_BLANK = "Department Name cannot be null, empty or blank";
    public static final String KEY_DEPARTMENT_NAME_NULL_OR_BLANK = "exception.input.validation.department.name.null.empty.or.blank";
    public static final String DEPARTMENT_NAME_LENGTH_CONSTRAINT = "Department Name cannot exceed 255 characters";
    public static final String KEY_DEPARTMENT_NAME_LENGTH_CONSTRAINT = "exception.input.validation.department.name.over.max.length";

    //Team
    public static final String TEAM_NAME_NULL_OR_BLANK = "Team Name cannot be null, empty or blank";
    public static final String KEY_TEAM_NAME_NULL_OR_BLANK = "exception.input.validation.team.name.null.empty.or.blank";
    public static final String TEAM_NOT_FOUND = "Team not found";
    public static final String KEY_TEAM_NOT_FOUND = "exception.resource.not.found.team";

    //Position
    public static final String POSITION_NAME_NULL_OR_BLANK = "Position Name cannot be null, empty or blank";
    public static final String KEY_POSITION_NAME_NULL_OR_BLANK = "exception.input.validation.position.null.or.blank";
    public static final String POSITION_NAME_LENGTH_CONSTRAINT = "Position Name cannot less than 3 characters and more than 255 characters";
    public static final String KEY_POSITION_NAME_LENGTH_CONSTRAINT = "exception.input.validation.position.name.length.constraint";
    public static final String POSITION_NOTE_LENGTH_CONSTRAINT = "Position Note cannot exceed 2000 characters";
    public static final String KEY_POSITION_NOTE_LENGTH_CONSTRAINT = "exception.input.validation.position.note.length.constraint";
    public static final String POSITION_QUANTITY_NULL_OR_BLANK = "Position Quantity cannot be null, empty or blank";
    public static final String KEY_POSITION_QUANTITY_NULL_OR_BLANK = "exception.input.validation.position.quantity.null.or.blank";
    public static final String POSITION_NOT_FOUND = "Position not found";
    public static final String KEY_POSITION_NOT_FOUND = "exception.resource.not.found.position";
    public static final String DUPLICATED_POSITION_NAME = "Position duplicated";
    public static final String KEY_DUPLICATED_POSITION_NAME = "exception.input.validation.duplicated.position.name";
    public static final String POSITION_QUANTITY_GREATER_THAN_0 = "Position Quantity must be greater than 0";
    public static final String KEY_POSITION_QUANTITY_GREATER_THAN_0 = "exception.input.validation.quantity.greater.than.zero";
    public static final String POSITION_QUANTITY_LESS_THAN_100 = "Position Quantity must be less than 100";
    public static final String KEY_POSITION_QUANTITY_LESS_THAN_100 = "exception.input.validation.quantity.less.than.one.hundred";
    public static final String TOPIC_IS_NOT_BELONG_TO_THIS_SKILL = "Required Topic is not belong to this required skill";
    public static final String KEY_TOPIC_IS_NOT_BELONG_TO_THIS_SKILL = "exception.input.validation.topic.is.not.belong.to.this.skill";
    public static final String SKILL_LEVEL_MUST_BE_GREATER_THAN_0 = "Skill Level is required";
    public static final String KEY_SKILL_LEVEL_MUST_BE_GREATER_THAN_0 = "exception.input.validation.skill.level.must.be.greater.than.zero";
    public static final String SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE = "Skill Require is required";
    public static final String KEY_SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE = "exception.input.validation.skill.require.must.be.must.have.or.nice.to.have";
    public static final String TOPIC_LEVEL_MUST_BE_GREATER_THAN_0 = "Topic Level is required";
    public static final String KEY_TOPIC_LEVEL_MUST_BE_GREATER_THAN_0 = "exception.input.validation.topic.level.must.be.greater.than.zero";
    public static final String TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE = "Topic Require is required";
    public static final String KEY_TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE = "exception.input.validation.topic.require.must.be.must.have.or.nice.to.have";
    public static final String KEY_TOPIC_ID_DUPLICATED = "exception.input.validation.topic.id.duplicated";
    public static final String TOPIC_ID_DUPLICATED = "Topic is duplicated";
    public static final String KEY_SKILL_ID_DUPLICATED = "exception.input.validation.skill.id.duplicated";
    public static final String SKILL_ID_DUPLICATED = "Skill is duplicated";
    public static final String KEY_REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT = "exception.input.validation.required.skill.note.length.constraint";
    public static final String REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT = "Required Skill Note cannot exceed 2000 characters";
    public static final String KEY_REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT = "exception.input.validation.required.topic.note.length.constraint";
    public static final String REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT = "Required Topic Note cannot exceed 2000 characters";
    public static final String KEY_REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE = "exception.input.validation.required.skill.require.must.be.must.have";
    public static final String REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE = "Require of skill must be MUST HAVE";
    public static final String KEY_REQUIRED_SKILL_LEVEL_MUST_BE_MASTER = "exception.input.validation.required.skill.level.must.be.master";
    public static final String REQUIRED_SKILL_LEVEL_MUST_BE_MASTER = "Required Skill Level must be MASTER";
    public static final String NULL_SEARCH_WORD = "Search word cannot be null";
    public static final String KEY_NULL_SEARCH_WORD = "exception.input.validation.search.word.null";

    //Pagination
    public static final String PAGE_RANGE_NONPOSITIVE = "Page or size cannot be less than one";
    public static final String KEY_PAGE_RANGE_NONPOSITIVE = "exception.input.validation.page.range.less.one";
    public static final String PAGE_OUT_OF_RANGE = "Page out of range";
    public static final String KEY_PAGE_OUT_OF_RANGE = "exception.resource.not.found.page.out.of.range";

    public static final int MAX_QUANTITY = 99;
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_SIZE_NAME = 255;
    public static final int MIN_SIZE_NAME = 3;
    public static final int MAX_SIZE_DESCRIPTION = 2000;
    public static final int MAX_SIZE_NOTE = 2000;

    private ErrorMessage() {
    }

    static Map<String, String> errorKeyAndMessageMap() {
        Map<String, String> errorMap = new HashMap<>();

        errorMap.put(KEY_SKILL_NAME_NULL_OR_BLANK, SKILL_NAME_NULL_OR_BLANK);
        errorMap.put(KEY_SKILL_NAME_LENGTH_CONSTRAINT, SKILL_NAME_LENGTH_CONSTRAINT);
        errorMap.put(KEY_SKILL_DESCRIPTION_LENGTH_CONSTRAINT, SKILL_DESCRIPTION_LENGTH_CONSTRAINT);

        errorMap.put(KEY_TOPIC_NAME_BLANK_OR_NULL, TOPIC_NAME_BLANK_OR_NULL);
        errorMap.put(KEY_TOPIC_NAME_LENGTH_CONSTRAINT, TOPIC_NAME_LENGTH_CONSTRAINT);
        errorMap.put(KEY_TOPIC_DESCRIPTION_LENGTH_CONSTRAINT, TOPIC_DESCRIPTION_LENGTH_CONSTRAINT);

        errorMap.put(KEY_USER_NAME_LENGTH_CONSTRAINT, USER_NAME_LENGTH_CONSTRAINT);
        errorMap.put(KEY_EMAIL_BLANK_OR_NULL, EMAIL_BLANK_OR_NULL);
        errorMap.put(KEY_EMAIL_WRONG_FORMAT, EMAIL_WRONG_FORMAT);
        errorMap.put(KEY_PASSWORD_BLANK_OR_NULL, PASSWORD_BLANK_OR_NULL);

        errorMap.put(KEY_DEPARTMENT_NAME_NULL_OR_BLANK, DEPARTMENT_NAME_NULL_OR_BLANK);
        errorMap.put(KEY_DEPARTMENT_NAME_LENGTH_CONSTRAINT, DEPARTMENT_NAME_LENGTH_CONSTRAINT);

        errorMap.put(KEY_TEAM_NAME_NULL_OR_BLANK, TEAM_NAME_NULL_OR_BLANK);

        errorMap.put(KEY_POSITION_NAME_NULL_OR_BLANK, POSITION_NAME_NULL_OR_BLANK);
        errorMap.put(KEY_POSITION_NAME_LENGTH_CONSTRAINT, POSITION_NAME_LENGTH_CONSTRAINT);
        errorMap.put(KEY_POSITION_NOTE_LENGTH_CONSTRAINT, POSITION_NOTE_LENGTH_CONSTRAINT);
        errorMap.put(KEY_POSITION_QUANTITY_NULL_OR_BLANK, POSITION_QUANTITY_NULL_OR_BLANK);
        errorMap.put(KEY_POSITION_QUANTITY_GREATER_THAN_0, POSITION_QUANTITY_GREATER_THAN_0);
        errorMap.put(KEY_POSITION_QUANTITY_LESS_THAN_100, POSITION_QUANTITY_LESS_THAN_100);

        errorMap.put(KEY_REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT, REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT);
        errorMap.put(KEY_REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT, REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT);

        errorMap.put(KEY_DATE_INVALID, DATE_INVALID);
        errorMap.put(KEY_NULL_SEARCH_WORD, NULL_SEARCH_WORD);
        return errorMap;
    }
}
