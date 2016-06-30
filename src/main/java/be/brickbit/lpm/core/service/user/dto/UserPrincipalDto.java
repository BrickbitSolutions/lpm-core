package be.brickbit.lpm.core.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPrincipalDto {
    private String username;
    private String mood;
    private List<String> authorities;
}
