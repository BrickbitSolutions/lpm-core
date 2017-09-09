package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import be.brickbit.lpm.core.controller.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;

@Component
public class AdminUserDetailsDtoMapper implements Converter<User, AdminUserDetailsDto> {
    @Override
    public AdminUserDetailsDto convert(User user) {
        return new AdminUserDetailsDto(
                user.getId(),
                user.getUsername(),
                user.getSeatNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate(),
                user.isEnabled(),
                !user.isAccountNonLocked(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList())
        );
    }
}
