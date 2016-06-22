package be.brickbit.lpm.core.service.user.impl;

import java.math.BigDecimal;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.brickbit.lpm.core.auth.exceptions.UserExistsException;
import be.brickbit.lpm.core.command.home.NewUserCommand;
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
        user.setEnabled(true);
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
