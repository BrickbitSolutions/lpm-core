package be.brickbit.lpm.core.service.user.impl;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.user.mapper.UserPrincipalDtoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}