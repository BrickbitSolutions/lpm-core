package be.brickbit.lpm.core.service.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPrincipalDto {
    private String username;
    private String mood;
    private List<String> authorities;
}
