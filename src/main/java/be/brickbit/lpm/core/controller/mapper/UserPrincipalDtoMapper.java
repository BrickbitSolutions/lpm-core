package be.brickbit.lpm.core.controller.mapper;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;

@Component
public class UserPrincipalDtoMapper implements UserDtoMapper<UserPrincipalDto> {
    @Override
    public UserPrincipalDto map(User user) {
        return new UserPrincipalDto(
                user.getId(),
                user.getUsername(),
                user.getMood(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList())
        );
    }
}
