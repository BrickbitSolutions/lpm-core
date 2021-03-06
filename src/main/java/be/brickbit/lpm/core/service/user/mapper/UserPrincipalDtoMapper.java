package be.brickbit.lpm.core.service.user.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;

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
