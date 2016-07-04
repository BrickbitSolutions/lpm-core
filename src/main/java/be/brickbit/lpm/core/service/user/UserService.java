package be.brickbit.lpm.core.service.user;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.mapper.UserMapper;
import be.brickbit.lpm.infrastructure.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, Service<User> {
    void createUser(NewUserCommand userCommand);
    <T> T findByUsername(String username, UserMapper<T> dtoMapper);
    void enableUser(Long id);
    void disableUser(Long id);
    void lockUser(Long id);
    void unlockUser(Long id);
    void updateAccountDetails(Long id, UpdateAccountDetailsCommand command);
}