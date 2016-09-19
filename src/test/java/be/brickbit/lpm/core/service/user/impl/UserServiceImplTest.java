package be.brickbit.lpm.core.service.user.impl;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.user.mapper.UserPrincipalDtoMapper;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserPrincipalDtoMapper dtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateUser() throws Exception {
        NewUserCommand command = NewUserCommandFixture.mutable();
        String hashedPassword = randomString();

        when(passwordEncoder.encode(command.getPassword())).thenReturn(hashedPassword);
        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.<User>empty());
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.<User>empty());

        userService.createUser(command);

        verify(userRepository).save(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertThat(user.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(user.getLastName()).isEqualTo(command.getLastName());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getPassword()).isEqualTo(hashedPassword);
        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getAuthorities().contains(AuthorityFixture.user()));
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.getMood()).isEqualTo("Hello LPM.");
    }

    @Test
    public void updateUserAccountDetails() throws Exception {
        UpdateAccountDetailsCommand command = new UpdateAccountDetailsCommand(
                randomString(),
                randomEmail(),
                Lists.newArrayList("ROLE_USER", "ROLE_ADMIN")
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();
        final Authority userRole = AuthorityFixture.user();
        final Authority adminRole = AuthorityFixture.admin();

        when(userRepository.findOne(userId)).thenReturn(user);
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(authorityRepository.findByAuthority("ROLE_ADMIN")).thenReturn(adminRole);

        userService.updateAccountDetails(userId, command);

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getAuthorities()).containsOnly(userRole, adminRole);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateUserAccountDetails__invalidRoleName() throws Exception {
        UpdateAccountDetailsCommand command = new UpdateAccountDetailsCommand(
                randomString(),
                randomEmail(),
                Lists.newArrayList("ROLE_USER", "ROLE_JAY")
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();
        final Authority userRole = AuthorityFixture.user();

        when(userRepository.findOne(userId)).thenReturn(user);
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(authorityRepository.findByAuthority("ROLE_JAY")).thenReturn(null);

        userService.updateAccountDetails(userId, command);
        assertThat(user.getAuthorities()).containsOnly(userRole);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = randomString();
        Optional<User> user = Optional.of(UserFixture.mutable());

        when(userRepository.findByUsername(username)).thenReturn(user);
        assertThat(userService.loadUserByUsername(username)).isEqualTo(user.get());
    }

    @Test
    public void findByUsername() throws Exception {
        String username = randomString();
        Optional<User> user = Optional.of(UserFixture.mutable());
        UserPrincipalDto dto = new UserPrincipalDto();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(dtoMapper.map(user.get())).thenReturn(dto);

        assertThat(userService.findByUsername(username, dtoMapper)).isSameAs(dto);
    }

    @Test
    public void findByUsername__NotFound() throws Exception {
        expectedException.expect(UsernameNotFoundException.class);

        String username = randomString();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.findByUsername(username, dtoMapper);
    }

    @Test
    public void findBySeatNumber() throws Exception {
        Integer seatNumber = randomInt();
        Optional<User> user = Optional.of(UserFixture.mutable());
        UserPrincipalDto dto = new UserPrincipalDto();

        when(userRepository.findBySeatNumber(seatNumber)).thenReturn(user);
        when(dtoMapper.map(user.get())).thenReturn(dto);

        assertThat(userService.findBySeatNumber(seatNumber, dtoMapper)).isSameAs(dto);
    }

    @Test
    public void findBySeatNumber__NotFound() throws Exception {
        expectedException.expect(UsernameNotFoundException.class);

        Integer seatNumber = randomInt();

        when(userRepository.findBySeatNumber(seatNumber)).thenReturn(Optional.empty());

        userService.findBySeatNumber(seatNumber, dtoMapper);
    }

    @Test
    public void testEnableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testEnableUser__userAlreadyEnabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        verify(userRepository, times(0)).save(user);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testDisableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testDisableUser__alreadyDisabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        verify(userRepository, times(0)).save(user);
        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testLockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testLockUser__AlreadyLocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        verify(userRepository, times(0)).save(user);
        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testUnlockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testUnlockUser__AlreadyUnlocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(userRepository.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        verify(userRepository, times(0)).save(user);
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void findAllAuthorities() throws Exception {
        final ArrayList<Authority> authorities = Lists.newArrayList(
                AuthorityFixture.admin(),
                AuthorityFixture.user()
        );
        when(authorityRepository.findAll()).thenReturn(authorities);

        List<String> result = userService.findAllAuthorities();

        assertThat(result).containsOnly(authorities.get(0).getAuthority(), authorities.get(1).getAuthority());
    }

    @Test
    public void testAssignSeatNr() throws Exception {
        final Long randomId = randomLong();
        final User user = UserFixture.mutable();
        final Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.empty());
        when(userRepository.findOne(randomId)).thenReturn(user);

        userService.assignSeat(randomId, seatNr);

        assertThat(user.getSeatNumber()).isEqualTo(seatNr);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAssignSeatNr_AlreadyAssigned() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Seat Number already assigned to another user");

        final Long randomId = randomLong();
        final User user = UserFixture.mutable();
        final Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.of(user));

        userService.assignSeat(randomId, seatNr);
    }

    @Test
    public void updatesUserProfile() throws Exception {
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                randomString(),
                randomEmail(),
                randomString()
        );
        User user = UserFixture.mutable();
        Long userId = randomLong();

        when(userRepository.findOne(userId)).thenReturn(user);

        userService.updateUserProfile(userId, command);

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getMood()).isEqualTo(command.getMood());

        verify(userRepository, times(1)).save(user);
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

        verify(userRepository, times(1)).save(user);
    }
}