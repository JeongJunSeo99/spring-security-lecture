package com.swcamp9th.springsecuritypratice.member.command.application.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReqSignupDTO {
    private String email;
    private String password;
    private String name;

    @JsonProperty
    private boolean EmailApprove;

}
