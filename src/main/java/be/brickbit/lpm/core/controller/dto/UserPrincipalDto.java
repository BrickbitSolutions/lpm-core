package be.brickbit.lpm.core.controller.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserPrincipalDto {
    private Long id;
    private String username;
    private String mood;
    private List<String> authorities;
}
