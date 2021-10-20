package com.daar.indexcv.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String message;
    private String path;
    private final HashMap<String, Object> errorDetail = new HashMap<>();

    public ErrorResponse(int status, String message, String path, Map<String, Object> errorDetail) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now().toString();
        if (!ObjectUtils.isEmpty(errorDetail)) {
            this.errorDetail.putAll(errorDetail);
        }
    }

    public ErrorResponse(BaseException ex, String path) {
        this(ex.getErrorCode().getStatus().value(),
                ex.getErrorCode().getMessage(),
                path,
                ex.getData());
    }

    public ErrorResponse(ErrorCode errorCode, String path, Map<String, Object> errorDetail) {
        this(errorCode.getStatus().value(), errorCode.getMessage(), path, errorDetail);
    }

}
