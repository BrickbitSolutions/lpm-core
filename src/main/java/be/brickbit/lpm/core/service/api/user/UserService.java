package be.brickbit.lpm.core.service.api.user;

import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void createUser(NewUserCommand userCommand);

    <T> T findOne(Long id, UserDtoMapper<T> dtoMapper);

    <T> List<T> findAll(UserDtoMapper<T> dtoMapper);

    <T> T findByUsername(String username, UserDtoMapper<T> dtoMapper);

    <T> T findBySeatNumber(Integer seatNumber, UserDtoMapper<T> dtoMapper);

    void enableUser(Long id);

    void disableUser(Long id);

    void lockUser(Long id);

    void unlockUser(Long id);

    void updateAccountDetails(Long id, UpdateAccountDetailsCommand command);

    void assignSeat(Long id, Integer seatNr);

    void updateUserProfile(Long id, UpdateUserProfileCommand updateUserProfileCommand);

    void updateUserPassword(Long id, UpdateUserPasswordCommand updateUserPasswordCommand);
}