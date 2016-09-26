package be.brickbit.lpm.core.controller.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserPrincipalDto {
    private Long id;
    private String username;
    private Long age;
    private Integer seatNumber;
    private String mood;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> authorities;
}
