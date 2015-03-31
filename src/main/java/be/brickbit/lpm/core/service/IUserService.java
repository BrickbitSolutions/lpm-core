package be.brickbit.lpm.core.service;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDetails getUserByName(String username);
    void createUser(NewUserCommand userCommand);
}