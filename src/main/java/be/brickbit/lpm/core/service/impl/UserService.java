package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.auth.exceptions.UserExistsException;
import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.model.User;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails getUserByName(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username);
    }

    @Override
    public void createUser(NewUserCommand userCommand){

        if(userRepository.findByUsername(userCommand.getUsername()) != null){
            throw new UserExistsException("Username already taken");
        }

        if(userRepository.findByEmail(userCommand.getEmail()) != null){
            throw new UserExistsException("A user was already registered with the given email");
        }

        userRepository.save(new User(
                userCommand.getUsername(),
                passwordEncoder.encode(userCommand.getPassword()),
                userCommand.getFirstName(),
                userCommand.getLastName(),
                userCommand.getEmail(),
                Collections.singleton(authorityRepository.findByAuthority("ROLE_USER"))
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}