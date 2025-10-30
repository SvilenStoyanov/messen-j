package com.svistoyanov.mj.constants;

public class Constants {
    private Constants() {
    }

    public static final String EMAIL_PATTERN                  = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
    public static final String EMAIL_PATTERN_ERROR_MESSAGE    = "Email should be a valid address!";
    public static final String EMAIL_NULL_ERROR_MESSAGE       = "Email should not be empty!";
    public static final String USERNAME_PATTERN               = "^\\S+$";
    public static final String USERNAME_PATTERN_ERROR_MESSAGE = "Username should be one word only!";
    public static final String USERNAME_NULL_ERROR_MESSAGE    = "Username should not be empty!";
    public static final String USERNAME_LENGTH_ERROR_MESSAGE  = "Username length must be between 3 and 20 characters!";
    public static final String PASSWORD_EMPTY_ERROR_MESSAGE   = "Password should not be empty!";
    public static final String PASSWORD_LENGTH_ERROR_MESSAGE  = "Password length must be between 3 and 20 characters!";

    public static final String AUTHOR_NULL_ERROR_MESSAGE    = "Author should not be empty!";
    public static final String RECIPIENT_NULL_ERROR_MESSAGE = "Recipient should not be empty!";
    public static final String CONTENT_BLANK_ERROR_MESSAGE  = "content must not be blank";
    public static final String OVER_THE_MAX_LENGTH_CONTENT  = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed id luctus odio. Integer id rhoncus quam. Vivamus bibendum non enim sed malesuada. Nam ut metus fermentum elit placerat lacinia. Ut feugiat leo id erat laoreet, ac pharetra sapien viverra. Aliquam in pulvinar lorem. Suspendisse dictum urna et mi fermentum sagittis. Cras volutpat sapien a egestas ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies est eu nibh consequat gravida. Duis convallis volutpat blandit.";

    public static final String OVER_THE_MAX_LENGTH_CONTENT_ERROR_MESSAGE = "length must be between 1 and 500";
    public static final String SENT_AT_ERROR_MESSAGE                     = "must not be null";

}
