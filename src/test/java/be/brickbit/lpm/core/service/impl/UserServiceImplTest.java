package be.brickbit.lpm.core.service.impl;

import com.google.common.collect.Lists;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.ActivationToken;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.ActivationTokenFixture;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;
import be.brickbit.lpm.core.repository.ActivationTokenRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import be.brickbit.lpm.mail.MailService;
import be.brickbit.lpm.mail.MailTemplateService;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomInt;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ActivationTokenRepository activationTokenRepository;
    @Mock
    private MailService mailService;
    @Mock
    private MailTemplateService mailTemplateService;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<ActivationToken> activationTokenArgumentCaptor;

    @Test
    public void testCreateUser() throws Exception {
        NewUserCommand command = NewUserCommandFixture.mutable();
        Authority authority = AuthorityFixture.user();
        String hashedPassword = randomString();

        when(passwordEncoder.encode(command.getPassword())).thenReturn(hashedPassword);
        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.empty());
        when(authorityService.findByAuthority("ROLE_USER")).thenReturn(authority);

        userService.createUser(command);

        verify(userRepository).save(userArgumentCaptor.capture());
        verify(activationTokenRepository).save(activationTokenArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertThat(user.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(user.getLastName()).isEqualTo(command.getLastName());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getPassword()).isEqualTo(hashedPassword);
        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getBirthDate()).isEqualTo(command.getBirthDate());
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getMood()).isEqualTo("Hello LPM.");
        assertThat(user.getAuthorities()).containsExactly(authority);

        ActivationToken activationToken = activationTokenArgumentCaptor.getValue();

        assertThat(activationToken.getToken()).isNotNull();
        assertThat(activationToken.getUser()).isEqualTo(user);

        verify(mailTemplateService, times(1)).createActivationMail(anyString(), anyString());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldNotSaveUserIfEmailIsAlreadyRegistered() throws Exception {
        NewUserCommand command = NewUserCommandFixture.mutable();

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(String.format("An account for '%s' already exists.", command.getEmail()));

        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.of(UserFixture.mutable()));

        userService.createUser(command);
    }

    @Test
    public void shouldNotSaveUserIUsernameAlreadyExists() throws Exception {
        NewUserCommand command = NewUserCommandFixture.mutable();

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(String.format("'%s' already exists.", command.getUsername()));

        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.of(UserFixture.mutable()));

        userService.createUser(command);
    }

    @Test
    public void findsUserById() throws Exception {
        Long userId = randomLong();
        User user = UserFixture.mutable();

        when(userRepository.findOne(userId)).thenReturn(user);

        assertThat(userRepository.findOne(userId)).isSameAs(user);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWithFindOne() throws Exception {
        final Long userId = randomLong();
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("User #%d not found", userId));
        userService.findOne(userId);
    }

    @Test
    public void findsAll() throws Exception {
        User user = UserFixture.mutable();
        Pageable pageRequest = new PageRequest(0, 5);

        when(userRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Lists.newArrayList(user)));

        assertThat(userService.findAll(pageRequest).getContent()).containsExactly(user);
    }

    @Test
    public void updateUserAccountDetails() throws Exception {
        UpdateAuthoritiesCommand command = new UpdateAuthoritiesCommand(
                Lists.newArrayList("ROLE_USER", "ROLE_ADMIN")
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();
        final Authority userRole = AuthorityFixture.user();
        final Authority adminRole = AuthorityFixture.admin();

        when(userRepository.findOne(userId)).thenReturn(user);
        when(authorityService.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(authorityService.findByAuthority("ROLE_ADMIN")).thenReturn(adminRole);

        userService.updateAuthorities(userId, command);

        assertThat(user.getAuthorities()).containsOnly(userRole, adminRole);
    }

    @Test
    public void updateUserAccountDetails__invalidRoleName() throws Exception {
        UpdateAuthoritiesCommand command = new UpdateAuthoritiesCommand(
                Lists.newArrayList("ROLE_USER", "ROLE_JAY")
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();
        final Authority userRole = AuthorityFixture.user();

        when(userRepository.findOne(userId)).thenReturn(user);
        when(authorityService.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(authorityService.findByAuthority("ROLE_JAY")).thenThrow(new EntityNotFoundException());

        expectedException.expect(EntityNotFoundException.class);

        userService.updateAuthorities(userId, command);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = randomString();
        User user = UserFixture.mutable();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertThat(userService.loadUserByUsername(username)).isEqualTo(user);
    }

    @Test
    public void findsByUsername() throws Exception {
        User user = UserFixture.mutable();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThat(userService.findByUsername(user.getUsername())).isSameAs(user);
    }

    @Test
    public void shouldThrowExceptionWhenUsernameNotFound() throws Exception {
        User user = UserFixture.mutable();
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("User '%s' not found", user.getUsername()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        userService.findByUsername(user.getUsername());
    }

    @Test
    public void throwsUsernameNotFoundExceptionOnLoadByUsername() throws Exception {
        String username = randomString();

        expectedException.expect(UsernameNotFoundException.class);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.loadUserByUsername(username);
    }


    @Test
    public void findsBySeatNumber() throws Exception {
        User user = UserFixture.mutable();

        when(userRepository.findBySeatNumber(user.getSeatNumber())).thenReturn(Optional.of(user));

        assertThat(userService.findBySeatNumber(user.getSeatNumber())).isSameAs(user);
    }

    @Test
    public void throwsEntityNotFoundOnNonExistentSeatNumber() throws Exception {
        Integer seatNumber = randomInt();

        expectedException.expect(EntityNotFoundException.class);

        when(userRepository.findBySeatNumber(seatNumber)).thenThrow(new EntityNotFoundException());

        userService.findBySeatNumber(seatNumber);
    }

    @Test
    public void testEnableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void doesNotEnableUserWhenUserIsAlreadyEnabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testDisableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testDisableUser__alreadyDisabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testLockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void doesNotLockUserWhenUserIsAlreadyLocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testUnlockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void doesNotUnlockUserWhenUserIsAlreadyUnlocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void assignsSeatToUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.empty());
        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.assignSeat(user.getId(), seatNr);

        assertThat(user.getSeatNumber()).isEqualTo(seatNr);
    }

    @Test
    public void shouldThrowExceptionWhenSeatIsAlreadyTaken() throws Exception {
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Seat Number already assigned to another user");

        User user = UserFixture.mutable();
        user.setId(randomLong());
        Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.of(user));
        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.assignSeat(user.getId(), seatNr);
    }

    @Test
    public void updatesUserProfile() throws Exception {
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                randomString(),
                randomString()
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();

        when(userRepository.findOne(userId)).thenReturn(user);

        userService.updateUserProfile(userId, command);

        assertThat(user.getMood()).isEqualTo(command.getMood());
        assertThat(user.getMobileNr()).isEqualTo(command.getMobileNr());
    }

    @Test
    public void updatesUserPassword() throws Exception {
        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(randomString());
        User user = UserFixture.mutable();
        Long userId = randomLong();
        String hashedPassword = randomString();

        when(passwordEncoder.encode(command.getPassword())).thenReturn(hashedPassword);
        when(userRepository.findOne(userId)).thenReturn(user);

        userService.updateUserPassword(userId, command);

        assertThat(user.getPassword()).isEqualTo(hashedPassword);
    }

    @Test
    public void updatesUserEmail() throws Exception {
        UpdateUserEmailCommand command = new UpdateUserEmailCommand(randomString());
        User user = UserFixture.mutable();
        Long userId = randomLong();

        when(userRepository.findOne(userId)).thenReturn(user);

        userService.updateUserEmail(userId, command);

        assertThat(user.getEmail()).isEqualTo(command.getEmail());
    }

    @Test
    public void activatesUserByToken() throws Exception {
        String token = randomString();
        ActivationToken activationToken = ActivationTokenFixture.mutable();
        activationToken.getUser().setEnabled(false);

        when(activationTokenRepository.findByToken(token)).thenReturn(activationToken);

        userService.activateUser(token);

        assertThat(activationToken.getUser().isEnabled()).isTrue();
        verify(activationTokenRepository, times(1)).delete(activationToken);
    }

    @Test
    public void resetsPassword() throws Exception {
        User user = UserFixture.mutable();
        String oldPassword = randomString();
        user.setPassword(oldPassword);
        Long userId = randomLong();

        when(userRepository.findOne(userId)).thenReturn(user);

        userService.resetPassword(userId);

        assertThat(user.getPassword()).isNotEqualTo(oldPassword);
        verify(mailTemplateService, times(1)).createPasswordResetMessage(anyString(), anyString());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }
}