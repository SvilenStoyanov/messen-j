package com.svistoyanov.mj.constants;

public class WorkerConstants {

    //    ChatEndpointWorker
    public static final String SEND_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE    = "AuthorId cannot be empty!";
    public static final String SEND_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE = "RecipientId cannot be empty!";
    public static final String SEND_MESSAGE_CONTENT_CANNOT_BE_NULL_ERROR_MESSAGE      = "content must not be empty.";
    public static final String SEND_MESSAGE_CONTENT_CANNOT_BE_BLANK_ERROR_MESSAGE     = "content must not be empty";
    public static final String SEND_MESSAGE_MESSAGE_INPUT_IS_INCORRECT_ERROR_MESSAGE  = "Message input is incorrect.";
    public static final String INVALID_REQUEST_ERROR_MESSAGE                          = "Invalid request.";
    public static final String LOAD_MESSAGE_AUTHOR_ID_CANNOT_BE_NULL_ERROR_MESSAGE    = "AuthorId cannot be empty!";
    public static final String LOAD_MESSAGE_RECIPIENT_ID_CANNOT_BE_NULL_ERROR_MESSAGE = "RecipientId cannot be empty!";
    public static final String LOAD_MESSAGE_START_ERROR_MESSAGE                       = "Start must be equal or greater than 0.";
    public static final String LOAD_MESSAGE_OFFSET_ERROR_MESSAGE                      = "Offset must be equal or greater than 1.";
    public static final String LOAD_MESSAGE_ERROR_AUTHORID_IS_EQUAL_TO_RECIPIENTID    = "AuthorId should be different from recipientId.";
    public static final String UNEXPECTED_ERROR_MESSAGE                               = "Unexpected error. Please try again.";

    //    AdministrationEndpointWorker
    public static final String SIGNUP_USER_EXISTS_ERROR_MESSAGE    = "Email address already in use!";
    public static final String SIGNUP_EMPTY_FIELDS_ERROR_MESSAGE   = "Required fields cannot be empty!";
    public static final String SIGNUP_INVALID_FIELDS_ERROR_MESSAGE = "Email, Username or Password not valid!";
    public static final String SIGNIN_EMPTY_FIELDS_ERROR_MESSAGE   = "Required fields cannot be empty!";
    public static final String SIGNIN_USER_NOTFOUND_ERROR_MESSAGE  = "No such entity found!";
    public static final String SIGNIN_PASSWORD_ERROR_MESSAGE       = "Wrong password.";
    public static final String UNEXPECTED_ERROR_MESSAGE_TRY_AGAIN  = "Unexpected error. Please try again.";
    public static final String REQUEST_CANNOT_BE_NULL              = "Request can not be null.";
}
