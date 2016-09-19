package be.brickbit.lpm.core.service.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserPrincipalDto extends UserDetailsDto{
    private List<String> authorities;

    public UserPrincipalDto(Long id, String username, Long age, Integer seatNumber, String mood, String firstName, String lastName, String email, List<String> authorities) {
        super(id, username, age, seatNumber, mood, firstName, lastName, email);
        this.authorities = authorities;
    }
}
