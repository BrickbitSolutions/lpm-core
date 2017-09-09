package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.domain.User;

@Component
public class UserDetailsDtoMapper implements Converter<User, UserDetailsDto> {
    @Override
    public UserDetailsDto convert(User user) {
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
