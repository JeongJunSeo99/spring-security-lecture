package com.swcamp9th.springsecuritypratice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomErrorCode {
    //40000
    NO_APPROVE_EMAIL(40000,HttpStatus.BAD_REQUEST, "이메일 요청이 완료되지 않았습니다");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

}
