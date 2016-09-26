package be.brickbit.lpm.core.service.user.impl;

import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.controller.mapper.UserPrincipalDtoMapper;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.fixture.UserPrincipalDtoFixture;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;
import be.brickbit.lpm.core.service.impl.UserServiceImpl;
import be.brickbit.lpm.core.service.impl.internal.api.InternalAuthorityService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private InternalUserService internalUserService;
    @Mock
    private InternalAuthorityService internalAuthorityService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserPrincipalDtoMapper dtoMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void testCreateUser() throws Exception {
        NewUserCommand command = NewUserCommandFixture.mutable();
        String hashedPassword = randomString();

        when(passwordEncoder.encode(command.getPassword())).thenReturn(hashedPassword);

        userService.createUser(command);

        verify(internalUserService).createUser(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertThat(user.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(user.getLastName()).isEqualTo(command.getLastName());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getPassword()).isEqualTo(hashedPassword);
        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getBirthDate()).isEqualTo(command.getBirthDate());
    }

    @Test
    public void findsOne() throws Exception {
        UserPrincipalDto dto = UserPrincipalDtoFixture.mutable();
        User user = UserFixture.mutable();
        Long userId = randomLong();

        when(internalUserService.findOne(userId)).thenReturn(user);
        when(dtoMapper.map(user)).thenReturn(dto);

        assertThat(userService.findOne(userId, dtoMapper)).isSameAs(dto);
    }

    @Test
    public void findsAll() throws Exception {
        UserPrincipalDto dto = UserPrincipalDtoFixture.mutable();
        User user = UserFixture.mutable();

        when(internalUserService.findAll()).thenReturn(Lists.newArrayList(user));
        when(dtoMapper.map(user)).thenReturn(dto);

        assertThat(userService.findAll(dtoMapper)).containsExactly(dto);
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

        when(internalUserService.findOne(userId)).thenReturn(user);
        when(internalAuthorityService.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(internalAuthorityService.findByAuthority("ROLE_ADMIN")).thenReturn(adminRole);

        userService.updateAccountDetails(userId, command);

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getAuthorities()).containsOnly(userRole, adminRole);

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

        when(internalUserService.findOne(userId)).thenReturn(user);
        when(internalAuthorityService.findByAuthority("ROLE_USER")).thenReturn(userRole);
        when(internalAuthorityService.findByAuthority("ROLE_JAY")).thenThrow(new EntityNotFoundException());

        expectedException.expect(EntityNotFoundException.class);

        userService.updateAccountDetails(userId, command);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = randomString();
        User user = UserFixture.mutable();

        when(internalUserService.findByUsername(username)).thenReturn(user);
        assertThat(userService.loadUserByUsername(username)).isEqualTo(user);
    }

    @Test
    public void findByUsername() throws Exception {
        String username = randomString();
        User user = UserFixture.mutable();
        UserPrincipalDto dto = UserPrincipalDtoFixture.mutable();

        when(internalUserService.findByUsername(username)).thenReturn(user);
        when(dtoMapper.map(user)).thenReturn(dto);

        assertThat(userService.findByUsername(username, dtoMapper)).isSameAs(dto);
    }

    @Test
    public void findByUsername__NotFound() throws Exception {
        expectedException.expect(EntityNotFoundException.class);

        String username = randomString();

        when(internalUserService.findByUsername(username)).thenThrow(new EntityNotFoundException());

        userService.findByUsername(username, dtoMapper);
    }

    @Test
    public void findBySeatNumber() throws Exception {
        Integer seatNumber = randomInt();
        User user = UserFixture.mutable();
        UserPrincipalDto dto = UserPrincipalDtoFixture.mutable();

        when(internalUserService.findBySeatNumber(seatNumber)).thenReturn(user);
        when(dtoMapper.map(user)).thenReturn(dto);

        assertThat(userService.findBySeatNumber(seatNumber, dtoMapper)).isSameAs(dto);
    }

    @Test
    public void findBySeatNumber__NotFound() throws Exception {
        expectedException.expect(EntityNotFoundException.class);

        Integer seatNumber = randomInt();

        when(internalUserService.findBySeatNumber(seatNumber)).thenThrow(new EntityNotFoundException());

        userService.findBySeatNumber(seatNumber, dtoMapper);
    }

    @Test
    public void testEnableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testEnableUser__userAlreadyEnabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(true);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.enableUser(user.getId());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testDisableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testDisableUser__alreadyDisabled() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setEnabled(false);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.disableUser(user.getId());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testLockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testLockUser__AlreadyLocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.lockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testUnlockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(false);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testUnlockUser__AlreadyUnlocked() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setAccountNonLocked(true);

        when(internalUserService.findOne(user.getId())).thenReturn(user);

        userService.unlockUser(user.getId());

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testAssignSeatNr() throws Exception {
        final Long randomId = randomLong();
        final User user = UserFixture.mutable();
        final Integer seatNr = randomInt();

        when(internalUserService.findOne(randomId)).thenReturn(user);

        userService.assignSeat(randomId, seatNr);

        verify(internalUserService, times(1)).assignSeat(user, seatNr);
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

        when(internalUserService.findOne(userId)).thenReturn(user);

        userService.updateUserProfile(userId, command);

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getMood()).isEqualTo(command.getMood());
    }

    @Test
    public void updatesUserPassword() throws Exception {
        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(randomString());
        User user = UserFixture.mutable();
        Long userId = randomLong();
        String hashedPassword = randomString();

        when(passwordEncoder.encode(command.getPassword())).thenReturn(hashedPassword);
        when(internalUserService.findOne(userId)).thenReturn(user);

        userService.updateUserPassword(userId, command);

        assertThat(user.getPassword()).isEqualTo(hashedPassword);
    }
}