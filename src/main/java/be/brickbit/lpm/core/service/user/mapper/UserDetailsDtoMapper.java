package be.brickbit.lpm.core.service.user.mapper;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class UserDetailsDtoMapper implements UserMapper<UserDetailsDto> {
    @Override
    public UserDetailsDto map(User user) {
        if(user != null) {
            return new UserDetailsDto(
                    user.getUsername(),
                    user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS),
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
