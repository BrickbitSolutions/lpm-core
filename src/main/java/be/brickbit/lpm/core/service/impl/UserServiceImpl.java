package be.brickbit.lpm.core.service.impl;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.ActivationToken;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.ActivationTokenRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import be.brickbit.lpm.mail.MailService;
import be.brickbit.lpm.mail.MailTemplateService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final ActivationTokenRepository activationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final MailTemplateService mailTemplateService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            AuthorityService authorityService,
            ActivationTokenRepository activationTokenRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService,
            MailTemplateService mailTemplateService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.activationTokenRepository = activationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.mailTemplateService = mailTemplateService;
    }

    @Override
    public void createUser(NewUserCommand userCommand) {
        User user = new User();

        if (userRepository.findByUsername(userCommand.getUsername()).isPresent()) {
            throw new ServiceException(String.format("'%s' already exists.", userCommand.getUsername()));
        }

        if (userRepository.findByEmail(userCommand.getEmail()).isPresent()) {
            throw new ServiceException(String.format("An account for '%s' already exists.", userCommand.getEmail()));
        }

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setMood("Hello LPM.");
        user.setAuthorities(Sets.newHashSet(getDefaultRole()));
        user.setEmail(userCommand.getEmail());
        user.setUsername(userCommand.getUsername());
        user.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setBirthDate(userCommand.getBirthDate());

        userRepository.save(user);
        saveActivationToken(user);
    }

    private Authority getDefaultRole() {
        return authorityService.findByAuthority("ROLE_USER");
    }

    private void saveActivationToken(User user) {
        ActivationToken activationToken = new ActivationToken();

        activationToken.setUser(user);
        activationToken.setToken(UUID.randomUUID().toString());

        activationTokenRepository.save(activationToken);

        String message = mailTemplateService.createActivationMail(
                user.getUsername(),
                activationToken.getToken()
        );

        mailService.sendMail(
                user.getEmail(),
                "Account Activation",
                message
        );
    }

    @Override
    public User findOne(Long id) {
        return Optional.ofNullable(userRepository.findOne(id)).orElseThrow(
                () -> new EntityNotFoundException(String.format("User #%d not found", id))
        );
    }

    @Override
    public Page<User> findAll(Pageable pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    @Cacheable("usersByUsername")
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
    public void activateUser(String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token);

        if (activationToken != null) {
            activationToken.getUser().setEnabled(true);
            activationTokenRepository.delete(activationToken);
        } else {
            throw new ServiceException("Activation token is not valid or has expired.");
        }
    }

    @Override
    public void enableUser(Long id) {
        User user = findOne(id);
        if (!user.isEnabled()) {
            user.setEnabled(true);
        }
    }

    @Override
    public void disableUser(Long id) {
        User user = findOne(id);
        if (user.isEnabled()) {
            user.setEnabled(false);
        }
    }

    @Override
    public void lockUser(Long id) {
        User user = findOne(id);
        if (user.isAccountNonLocked()) {
            user.setAccountNonLocked(false);
        }
    }

    @Override
    public void unlockUser(Long id) {
        User user = findOne(id);
        if (!user.isAccountNonLocked()) {
            user.setAccountNonLocked(true);
        }
    }

    @Override
    @CacheEvict({"usersByUsername"})
    public void updateAuthorities(Long id, UpdateAuthoritiesCommand command) {
        User user = findOne(id);
        user.setAuthorities(
                command.getAuthorities().stream()
                        .map(authorityService::findByAuthority)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void assignSeat(Long id, Integer seatNr) {
        User user = findOne(id);

        if (userRepository.findBySeatNumber(seatNr).isPresent()) {
            throw new ServiceException("Seat Number already assigned to another user");
        } else {
            user.setSeatNumber(seatNr);
        }
    }

    @Override
    @CacheEvict({"usersByUsername"})
    public void updateUserProfile(Long id, UpdateUserProfileCommand command) {
        User user = findOne(id);

        user.setMood(command.getMood());
        user.setMobileNr(command.getMobileNr());
    }

    @Override
    public void updateUserPassword(Long id, UpdateUserPasswordCommand updateUserPasswordCommand) {
        User user = findOne(id);
        user.setPassword(passwordEncoder.encode(updateUserPasswordCommand.getPassword()));
    }

    @Override
    public void updateUserEmail(Long id, UpdateUserEmailCommand updateUserPasswordCommand) {
        User user = findOne(id);
        user.setEmail(updateUserPasswordCommand.getEmail());
    }

    @Override
    public void resetPassword(Long id) {
        String password = RandomStringUtils.random(24, true, true);

        User user = findOne(id);
        user.setPassword(passwordEncoder.encode(password));

        mailService.sendMail(
                user.getEmail(),
                "Password Reset",
                mailTemplateService.createPasswordResetMessage(
                        user.getUsername(),
                        password
                )
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return findByUsername(username);
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }
}
