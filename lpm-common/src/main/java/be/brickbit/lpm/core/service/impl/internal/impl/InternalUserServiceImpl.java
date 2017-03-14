package be.brickbit.lpm.core.service.impl.internal.impl;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InternalUserServiceImpl implements InternalUserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public InternalUserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public User findOne(Long userId) {
        return Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(
                () -> new EntityNotFoundException(String.format("User #%d not found", userId))
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ServiceException(String.format("'%s' already exists.", user.getUsername()));
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ServiceException(String.format("An account for '%s' already exists.", user.getEmail()));
        }

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setMood("Hello LPM.");
        user.setAuthorities(Sets.newHashSet(getDefaultRole()));

        userRepository.save(user);
    }

    private Authority getDefaultRole() {
        return authorityRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new ServiceException("Please provision the database with a default role 'ROLE_USER'")
                );
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("User '%s' not found", username))
        );
    }

    @Override
    public User findBySeatNumber(Integer seatNumber) {
        return userRepository.findBySeatNumber(seatNumber).orElseThrow(
                () -> new EntityNotFoundException(String.format("No user for seat #%d found", seatNumber))
        );
    }

    @Override
    public void assignSeat(User user, Integer seatNr) {
        if (userRepository.findBySeatNumber(seatNr).isPresent()) {
            throw new ServiceException("Seat Number already assigned to another user");
        } else {
            user.setSeatNumber(seatNr);
        }
    }
}
