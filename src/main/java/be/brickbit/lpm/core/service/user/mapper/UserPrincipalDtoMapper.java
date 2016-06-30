package be.brickbit.lpm.core.service.user.mapper;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserPrincipalDtoMapper implements UserMapper<UserPrincipalDto> {
    @Override
    public UserPrincipalDto map(User user) {
        return new UserPrincipalDto(
                user.getUsername(),
                user.getMood(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList())
        );
    }
}
