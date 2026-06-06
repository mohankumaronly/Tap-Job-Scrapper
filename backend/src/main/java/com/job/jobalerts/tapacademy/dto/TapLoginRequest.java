package com.job.jobalerts.tapacademy.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TapLoginRequest {
    private String email;
    private String password;
    private String username;
}