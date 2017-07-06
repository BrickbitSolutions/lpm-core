package be.brickbit.lpm.core.controller.mapper;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import be.brickbit.lpm.core.controller.dto.UserProfileDto;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;

@Component
public class UserProfileDtoMapper implements UserDtoMapper<UserProfileDto> {
    @Override
    public UserProfileDto map(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getSeatNumber(),
                user.getMood(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate().format(DateTimeFormatter.ISO_DATE),
                user.getEmail(),
                user.getMobileNr()
        );
    }
}
