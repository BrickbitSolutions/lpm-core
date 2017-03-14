package be.brickbit.lpm.core.controller.dto;

import lombok.Value;

@Value
public class BadgeDto {
    private String token;
    private Boolean enabled;
}
