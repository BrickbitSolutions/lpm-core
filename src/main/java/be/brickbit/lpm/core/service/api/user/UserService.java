package be.brickbit.lpm.core.service.api.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.User;

public interface UserService extends UserDetailsService {
    void createUser(NewUserCommand userCommand);

    User findOne(Long id);

    List<User> findAll();

    User findByUsername(String username);

    User findBySeatNumber(Integer seatNumberr);

    void activateUser(String token);

    void enableUser(Long id);

    void disableUser(Long id);

    void lockUser(Long id);

    void unlockUser(Long id);

    void updateAuthorities(Long id, UpdateAuthoritiesCommand command);

    void assignSeat(Long id, Integer seatNr);

    void updateUserProfile(Long id, UpdateUserProfileCommand updateUserProfileCommand);

    void updateUserPassword(Long id, UpdateUserPasswordCommand updateUserPasswordCommand);

    void updateUserEmail(Long id, UpdateUserEmailCommand updateUserPasswordCommand);

    void resetPassword(Long id);
}