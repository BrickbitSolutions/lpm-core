package be.brickbit.lpm.core.service.user.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserPrincipalDto extends UserDetailsDto{
    private List<String> authorities;

    public UserPrincipalDto(Long id, String username, Long age, Integer seatNumber, BigDecimal wallet, String mood, String firstName, String lastName, String email, List<String> authorities) {
        super(id, username, age, seatNumber, wallet, mood, firstName, lastName, email);
        this.authorities = authorities;
    }
}
