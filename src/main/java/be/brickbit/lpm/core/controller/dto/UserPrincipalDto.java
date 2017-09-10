package be.brickbit.lpm.core.controller.dto;

import java.util.List;

import lombok.Value;

@Value
public class UserPrincipalDto {
    private Long id;
    private String username;
    private String email;
    private String mood;
    private List<String> authorities;
}
