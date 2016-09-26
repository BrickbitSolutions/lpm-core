package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class UserPrincipalDtoMapper implements UserDtoMapper<UserPrincipalDto> {
    @Override
    public UserPrincipalDto map(User user) {
        return new UserPrincipalDto(
                user.getId(),
                user.getUsername(),
                user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS),
                user.getSeatNumber(),
                user.getMood(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList())
        );
    }
}
