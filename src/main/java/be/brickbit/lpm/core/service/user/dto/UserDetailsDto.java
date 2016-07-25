package be.brickbit.lpm.core.service.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
