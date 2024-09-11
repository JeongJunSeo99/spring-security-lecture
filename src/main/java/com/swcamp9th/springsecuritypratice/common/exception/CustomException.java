package com.swcamp9th.springsecuritypratice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final CustomErrorCode errorCode;

    @Override
    public String getMessage() {
        return this.errorCode.getMessage();
    }

}
