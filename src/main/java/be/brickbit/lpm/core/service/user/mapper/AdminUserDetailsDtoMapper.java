package be.brickbit.lpm.core.service.user.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.dto.AdminUserDetailsDto;

@Component
public class AdminUserDetailsDtoMapper implements UserMapper<AdminUserDetailsDto> {
	@Override
    public AdminUserDetailsDto map(User user) {
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
