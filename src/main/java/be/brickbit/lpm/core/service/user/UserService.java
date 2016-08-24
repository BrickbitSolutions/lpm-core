package be.brickbit.lpm.core.service.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.user.mapper.UserMapper;
import be.brickbit.lpm.infrastructure.Service;

public interface UserService extends UserDetailsService, Service<User> {
    void createUser(NewUserCommand userCommand);
    <T> T findByUsername(String username, UserMapper<T> dtoMapper);
    <T> T findBySeatNumber(Integer seatNumber, UserMapper<T> dtoMapper);
    void enableUser(Long id);
    void disableUser(Long id);
    void lockUser(Long id);
    void unlockUser(Long id);
    void updateAccountDetails(Long id, UpdateAccountDetailsCommand command);
    List<String> findAllAuthorities();
    void assignSeat(Long id, Integer seatNr);
    void updateUserProfile(Long id, UpdateUserProfileCommand updateUserProfileCommand);
}