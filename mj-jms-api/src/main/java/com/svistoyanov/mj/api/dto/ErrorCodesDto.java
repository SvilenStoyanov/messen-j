package com.svistoyanov.mj.api.dto;

public enum ErrorCodesDto {
    FORBIDDEN,
    NOT_FOUND,
    CONFLICT,
    VALIDATION,
    INTERNAL_SERVER_ERROR,

    /*
    FORBIDDEN - If the client has no rights to do the given operation
    NOT_FOUND - If the client tries to load entity which doesn't exists
    CONFLICT - If client tries to create entity which already exsist
    VALIDATION - If the request is invalid
    INTERNAL_SERVER_ERROR - Should happen very rarely and in exceptional cases
    */

}
