package be.brickbit.lpm.core.service.user.mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;

@Component
public class UserDetailsDtoMapper implements UserMapper<UserDetailsDto> {
    @Override
    public UserDetailsDto map(User user) {
        if(user != null) {
            return new UserDetailsDto(
                    user.getId(),
                    user.getUsername(),
                    user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS),
                    user.getSeatNumber(),
                    user.getWallet(),
                    user.getMood(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()
            );
        }else{
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
