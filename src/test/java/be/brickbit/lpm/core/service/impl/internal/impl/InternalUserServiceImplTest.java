package be.brickbit.lpm.core.service.impl.internal.impl;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomInt;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InternalUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private InternalUserServiceImpl internalUserService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findsUserById() throws Exception {
        Long userId = randomLong();
        User user = UserFixture.mutable();

        when(userRepository.findOne(userId)).thenReturn(user);

        assertThat(internalUserService.findOne(userId)).isSameAs(user);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWithFindOne() throws Exception {
        final Long userId = randomLong();
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("User #%d not found", userId));
        internalUserService.findOne(userId);
    }

    @Test
    public void findsAllUsers() throws Exception {
        List<User> users = Lists.newArrayList(
                UserFixture.mutable(),
                UserFixture.mutable()
        );

        when(userRepository.findAll()).thenReturn(users);

        assertThat(internalUserService.findAll()).isSameAs(users);
    }

    @Test
    public void savesUser() throws Exception {
        User user = UserFixture.mutable();

        Authority authority = AuthorityFixture.user();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.of(authority));

        internalUserService.createUser(user);

        verify(userRepository, times(1)).save(user);

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getMood()).isEqualTo("Hello LPM.");
        assertThat(user.getAuthorities()).containsExactly(authority);
    }

    @Test
    public void throwsExceptionOnMissingDatabaseProvision() throws Exception {
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Please provision the database with a default role 'ROLE_USER'");

        User user = UserFixture.mutable();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.empty());

        internalUserService.createUser(user);
    }

    @Test
    public void shouldNotSaveUserIfUsernameExists() throws Exception {
        User user = UserFixture.mutable();

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(String.format("'%s' already exists.", user.getUsername()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(UserFixture.mutable()));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.empty());

        internalUserService.createUser(user);
    }

    @Test
    public void shouldNotSaveUserIfEmailIsAlreadyRegistered() throws Exception {
        User user = UserFixture.mutable();

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(String.format("An account for '%s' already exists.", user.getEmail()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(UserFixture.mutable()));
        when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.empty());

        internalUserService.createUser(user);
    }

    @Test
    public void findsByUsername() throws Exception {
        User user = UserFixture.mutable();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThat(internalUserService.findByUsername(user.getUsername())).isSameAs(user);
    }

    @Test
    public void shouldThrowExceptionWhenUsernameNotFound() throws Exception {
        User user = UserFixture.mutable();
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("User '%s' not found", user.getUsername()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        internalUserService.findByUsername(user.getUsername());
    }

    @Test
    public void findsBySeatNumber() throws Exception {
        User user = UserFixture.mutable();

        when(userRepository.findBySeatNumber(user.getSeatNumber())).thenReturn(Optional.of(user));

        assertThat(internalUserService.findBySeatNumber(user.getSeatNumber())).isSameAs(user);
    }

    @Test
    public void findsByEmail() throws Exception {
        User user = UserFixture.mutable();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(internalUserService.findByEmail(user.getEmail())).isSameAs(user);
    }

    @Test
    public void shouldThrowExceptionWhenSeatNrNotFound() throws Exception {
        User user = UserFixture.mutable();
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("No user for seat #%d found", user.getSeatNumber()));

        when(userRepository.findBySeatNumber(user.getSeatNumber())).thenReturn(Optional.empty());

        internalUserService.findBySeatNumber(user.getSeatNumber());
    }

    @Test
    public void assignsSeatToUser() throws Exception {
        User user = UserFixture.mutable();
        Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.empty());

        internalUserService.assignSeat(user, seatNr);

        assertThat(user.getSeatNumber()).isEqualTo(seatNr);
    }

    @Test
    public void shouldThrowExceptionWhenSeatIsAlreadyTaken() throws Exception {
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Seat Number already assigned to another user");

        User user = UserFixture.mutable();
        Integer seatNr = randomInt();

        when(userRepository.findBySeatNumber(seatNr)).thenReturn(Optional.of(user));

        internalUserService.assignSeat(user, seatNr);
    }

}