package be.brickbit.lpm.core.service;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.mapper.UserMapper;
import be.brickbit.lpm.infrastructure.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService, Service<User> {
    void createUser(NewUserCommand userCommand);
    <T> T findByUserId(Long userId, UserMapper<T> dtoMapper);
    <T> T findByUsername(String username, UserMapper<T> dtoMapper);
}