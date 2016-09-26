package be.brickbit.lpm.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDetailsDto {
    private Long id;
    private String username;
    private Integer seatNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private Boolean enabled;
    private Boolean locked;
    private List<String> authorities;
}
