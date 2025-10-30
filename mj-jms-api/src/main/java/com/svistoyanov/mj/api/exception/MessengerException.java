package com.svistoyanov.mj.api.exception;

import com.svistoyanov.mj.api.dto.ErrorDto;

public class MessengerException extends RuntimeException {

    private ErrorDto errorDto;

    public MessengerException(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }

    public MessengerException(Throwable cause, ErrorDto errorDto) {
        super(cause);
        this.errorDto = errorDto;
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }

}
