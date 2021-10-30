package com.daar.indexcv.exceptions;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
import lombok.Getter;

import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "The file must be a *.pdf or *.docx"),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "The file cannot be empty"),
    METHOD_ARGUMENT_INVALID(HttpStatus.BAD_REQUEST,"The parameter cannot be null"),
    EMPTY_KEYWORD(HttpStatus.BAD_REQUEST,"The Keyword cannot be empty"),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "The article is not found");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}