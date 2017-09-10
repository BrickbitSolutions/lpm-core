package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;

@Component
public class UserPrincipalDtoMapper implements Converter<User, UserPrincipalDto> {
    @Override
    public UserPrincipalDto convert(User user) {
        return new UserPrincipalDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMood(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList())
        );
    }
}
