package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.ActivationToken;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.mail.MailService;
import be.brickbit.lpm.mail.MailTemplateService;
import be.brickbit.lpm.core.repository.ActivationTokenRepository;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalAuthorityService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final InternalUserService internalUserService;
    private final InternalAuthorityService internalAuthorityService;
    private final ActivationTokenRepository activationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final MailTemplateService mailTemplateService;

    @Autowired
    public UserServiceImpl(
            InternalUserService internalUserService,
            InternalAuthorityService internalAuthorityService,
            ActivationTokenRepository activationTokenRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService,
            MailTemplateService mailTemplateService) {
        this.internalUserService = internalUserService;
        this.internalAuthorityService = internalAuthorityService;
        this.activationTokenRepository = activationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.mailTemplateService = mailTemplateService;
    }

    @Override
    public void createUser(NewUserCommand userCommand) {
        User user = new User();

        user.setEmail(userCommand.getEmail());
        user.setUsername(userCommand.getUsername());
        user.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setBirthDate(userCommand.getBirthDate());

        internalUserService.createUser(user);
        saveActivationToken(user);
    }

    private void saveActivationToken(User user){
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
    public <T> T findOne(Long id, UserDtoMapper<T> dtoMapper) {
        return dtoMapper.map(getUser(id));
    }

    @Override
    public <T> List<T> findAll(UserDtoMapper<T> dtoMapper) {
        return internalUserService.findAll().stream().map(dtoMapper::map).collect(Collectors.toList());
    }

    @Override
    @Cacheable("usersByUsername")
    public <T> T findByUsername(String username, UserDtoMapper<T> dtoMapper) {
        return dtoMapper.map(
                internalUserService.findByUsername(username)
        );
    }

    @Override
    public <T> T findBySeatNumber(Integer seatNumber, UserDtoMapper<T> dtoMapper) {
        return dtoMapper.map(
                internalUserService.findBySeatNumber(seatNumber)
        );
    }

    @Override
    public void activateUser(String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token);

        if(activationToken != null) {
            activationToken.getUser().setEnabled(true);
            activationTokenRepository.delete(activationToken);
        }else{
            throw new ServiceException("Activation token is not valid or has expired.");
        }
    }

    @Override
    public void enableUser(Long id) {
        User user = internalUserService.findOne(id);
        if (!user.isEnabled()) {
            user.setEnabled(true);
        }
    }

    @Override
    public void disableUser(Long id) {
        User user = internalUserService.findOne(id);
        if (user.isEnabled()) {
            user.setEnabled(false);
        }
    }

    @Override
    public void lockUser(Long id) {
        User user = internalUserService.findOne(id);
        if (user.isAccountNonLocked()) {
            user.setAccountNonLocked(false);
        }
    }

    @Override
    public void unlockUser(Long id) {
        User user = internalUserService.findOne(id);
        if (!user.isAccountNonLocked()) {
            user.setAccountNonLocked(true);
        }
    }

    @Override
    @CacheEvict({"usersByUsername"})
    public void updateAuthorities(Long id, UpdateAuthoritiesCommand command) {
        User user = getUser(id);
        user.setAuthorities(
                command.getAuthorities().stream()
                        .map(internalAuthorityService::findByAuthority)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void assignSeat(Long id, Integer seatNr) {
        User user = internalUserService.findOne(id);
        internalUserService.assignSeat(user, seatNr);
    }

    @Override
    @CacheEvict({"usersByUsername"})
    public void updateUserProfile(Long id, UpdateUserProfileCommand command) {
        User user = getUser(id);

        user.setMood(command.getMood());
        user.setMobileNr(command.getMobileNr());
    }

    @Override
    public void updateUserPassword(Long id, UpdateUserPasswordCommand updateUserPasswordCommand) {
        User user = getUser(id);
        user.setPassword(passwordEncoder.encode(updateUserPasswordCommand.getPassword()));
    }

    @Override
    public void updateUserEmail(Long id, UpdateUserEmailCommand updateUserPasswordCommand) {
        User user = getUser(id);
        user.setEmail(updateUserPasswordCommand.getEmail());
    }

    @Override
    public void resetPassword(Long id) {
        String password = RandomStringUtils.random(24, true, true);

        User user = internalUserService.findOne(id);
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

    private User getUser(Long id) {
        return internalUserService.findOne(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return internalUserService.findByEmail(email);
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }
}
