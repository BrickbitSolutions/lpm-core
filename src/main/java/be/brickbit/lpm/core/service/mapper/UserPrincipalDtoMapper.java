package be.brickbit.lpm.core.service.mapper;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.dto.UserPrincipalDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserPrincipalDtoMapper implements UserMapper<UserPrincipalDto> {
    @Override
    public UserPrincipalDto map(User user) {
        UserPrincipalDto dto = new UserPrincipalDto();

        dto.setAuthorities(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
        dto.setMood("I will rule the universe");
        dto.setUsername(user.getUsername());

        return dto;
    }
}
