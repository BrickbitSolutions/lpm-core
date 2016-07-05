package be.brickbit.lpm.core.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailsDto {
    private String username;
    private Long age;
    private Integer seatNumber;
    private BigDecimal wallet;
    private String mood;
    private String firstName;
    private String lastName;
    private String email;
}
