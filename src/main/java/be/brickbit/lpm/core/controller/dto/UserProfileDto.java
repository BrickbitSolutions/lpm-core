package be.brickbit.lpm.core.controller.dto;

import lombok.Value;

@Value
public class UserProfileDto {
    private Long id;
    private String username;
    private Integer seatNumber;
    private String mood;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String mobilePhoneNr;
}
