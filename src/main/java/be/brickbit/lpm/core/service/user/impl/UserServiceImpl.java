package be.brickbit.lpm.core.service.user.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.brickbit.lpm.core.auth.exceptions.UserExistsException;
import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.user.UserService;
import be.brickbit.lpm.core.service.user.mapper.UserMapper;
import be.brickbit.lpm.infrastructure.AbstractService;

@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(NewUserCommand userCommand){
        if(userRepository.findByUsername(userCommand.getUsername()).isPresent()){
            throw new UserExistsException("Username already taken");
        }

        if(userRepository.findByEmail(userCommand.getEmail()).isPresent()){
            throw new UserExistsException("A user was already registered with the given email");
        }

        User user = new User();

        user.setEmail(userCommand.getEmail());
        user.setUsername(userCommand.getUsername());
        user.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setBirthDate(userCommand.getBirthDate());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setMood("Hello LPM.");
        user.setWallet(BigDecimal.ZERO);
        user.setAuthorities(Collections.singleton(authorityRepository.findByAuthority("ROLE_USER")));

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T findByUsername(String username, UserMapper<T> dtoMapper) {
        return dtoMapper.map(
                getRepository().findByUsername(username).orElseThrow(this::getUserNotFoundException)
        );
    }

    @Override
    public <T> T findBySeatNumber(Integer seatNumber, UserMapper<T> dtoMapper) {
        return dtoMapper.map(
                getRepository().findBySeatNumber(seatNumber).orElseThrow(this::getUserNotFoundException)
        );
    }

    @Override
    public void enableUser(Long id) {
        User user = userRepository.findOne(id);
        if(!user.isEnabled()){
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Override
    public void disableUser(Long id) {
        User user = userRepository.findOne(id);
        if(user.isEnabled()){
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public void lockUser(Long id) {
        User user = userRepository.findOne(id);
        if(user.isAccountNonLocked()){
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }

    @Override
    public void unlockUser(Long id) {
        User user = userRepository.findOne(id);
        if(!user.isAccountNonLocked()){
            user.setAccountNonLocked(true);
            userRepository.save(user);
        }
    }

    @Override
    public void updateAccountDetails(Long id, UpdateAccountDetailsCommand command) {
        User user = getUser(id);

        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setAuthorities(
                command.getAuthorities().stream()
                        .map(authorityRepository::findByAuthority)
                        .filter(authority -> authority != null)
                        .collect(Collectors.toSet())
        );

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getAuthority).collect(Collectors.toList());
    }

    @Override
    public void assignSeat(Long id, Integer seatNr) {
        if(userRepository.findBySeatNumber(seatNr).isPresent()){
            throw new RuntimeException("Seat Number already assigned to another user");
        }

        User user = getUser(id);

        user.setSeatNumber(seatNr);

        userRepository.save(user);
    }

    @Override
    public void updateUserProfile(Long id, UpdateUserProfileCommand command) {
        User user = getUser(id);

        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setMood(command.getMood());

        userRepository.save(user);
    }

    private User getUser(Long id) {
        return Optional.ofNullable(userRepository.findOne(id)).orElseThrow(this::getUserNotFoundException);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(this::getUserNotFoundException);
    }

    private UsernameNotFoundException getUserNotFoundException(){
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected UserRepository getRepository() {
        return userRepository;
    }
}
