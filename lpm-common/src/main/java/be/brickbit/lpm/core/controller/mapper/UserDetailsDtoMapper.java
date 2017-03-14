package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class UserDetailsDtoMapper implements UserDtoMapper<UserDetailsDto> {
    @Override
    public UserDetailsDto map(User user) {
        if (user != null) {
            return new UserDetailsDto(
                    user.getId(),
                    user.getUsername(),
                    user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS),
                    user.getSeatNumber(),
                    user.getMood()
            );
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
