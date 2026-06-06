package com.job.jobalerts.tapacademy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TapLoginResponse {
    @JsonProperty("user")
    private String user;
}