package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import be.brickbit.lpm.core.controller.dto.UserProfileDto;
import be.brickbit.lpm.core.domain.User;

@Component
public class UserProfileDtoMapper implements Converter<User, UserProfileDto> {
    @Override
    public UserProfileDto convert(User user) {
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
