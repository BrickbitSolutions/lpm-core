package be.brickbit.lpm.core.service.user.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
