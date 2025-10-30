package com.svistoyanov.mj.exception;

import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.exception.MessengerException;

import java.util.List;

public class ExceptionUtils {

    private ExceptionUtils() {
        //Static only
    }

    public static MessengerException createForbiddenError(String... messages) {
        return createBadRequest(ErrorCodesDto.FORBIDDEN, messages);
    }

    public static MessengerException createNotFoundError(String... messages) {
        return createBadRequest(ErrorCodesDto.NOT_FOUND, messages);
    }

    public static MessengerException createConflictError(String... messages) {
        return createBadRequest(ErrorCodesDto.CONFLICT, messages);
    }

    public static MessengerException createValidationError(String... messages) {
        return createBadRequest(ErrorCodesDto.VALIDATION, messages);
    }

    public static MessengerException createInternalServerError(String... messages) {
        return createBadRequest(ErrorCodesDto.INTERNAL_SERVER_ERROR, messages);
    }

    public static MessengerException createBadRequest(ErrorCodesDto errorCodesDto, String... messages) {
        return new MessengerException(
                new ErrorDto(errorCodesDto, List.of(messages)));
    }
}
