package be.brickbit.lpm.core.service.impl.internal.api;

import be.brickbit.lpm.core.domain.User;

import java.util.List;

public interface InternalUserService {
    User findOne(Long userId);

    List<User> findAll();

    void createUser(User user);

    User findByUsername(String username);

    User findBySeatNumber(Integer seatNumber);

    User findByEmail(String email);

    void assignSeat(User user, Integer seatNr);
}
