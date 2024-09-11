package com.swcamp9th.springsecuritypratice.common;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ResponseMessage {
    private int httpStatus;
    private String message;
    private Object result;
}
