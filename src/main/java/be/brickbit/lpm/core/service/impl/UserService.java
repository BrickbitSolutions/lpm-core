package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.auth.exceptions.UserExistsException;
import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.IUserService;
import be.brickbit.lpm.core.service.mapper.UserMapper;
import be.brickbit.lpm.infrastructure.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User> implements IUserService{
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

        userRepository.save(new User(
                userCommand.getUsername(),
                passwordEncoder.encode(userCommand.getPassword()),
                userCommand.getFirstName(),
                userCommand.getLastName(),
                userCommand.getBirthDate(),
                userCommand.getEmail(),
                Collections.singleton(authorityRepository.findByAuthority("ROLE_USER"))
        ));
    }

    @Override
    public <T> T findByUserId(Long userId, UserMapper<T> dtoMapper){
        return dtoMapper.map(
                Optional.ofNullable(getRepository().findOne(userId)).orElseThrow(this::getUserNotFoundException)
        );
    }

    @Override
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
    protected UserRepository getRepository() {
        return userRepository;
    }
}
