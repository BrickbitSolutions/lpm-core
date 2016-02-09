package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.repository.UserRepository;
import be.brickbit.lpm.core.service.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.mapper.UserPrincipalDtoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserPrincipalDtoMapper dtoMapper;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void testCreateUser() throws Exception {
        NewUserCommand command = new NewUserCommand();
        command.setFirstName("Jonas");
        command.setLastName("Liekens");
        command.setEmail("jonas.liekens@brickbit.be");
        command.setPassword("pwd");
        command.setUsername("user");

        when(passwordEncoder.encode("pwd")).thenReturn("pwd");
        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.<User>empty());
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.<User>empty());

        userService.createUser(command);

        verify(userRepository).save(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertThat(user.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(user.getLastName()).isEqualTo(command.getLastName());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getPassword()).isEqualTo(command.getPassword());
        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getAuthorities().contains(new Authority("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = "soulscammer";
        Optional<User> user = Optional.of(UserFixture.getUser());

        when(userRepository.findByUsername(username)).thenReturn(user);
        assertThat(userService.loadUserByUsername(username)).isEqualTo(user.get());
    }

    @Test
    public void findByUsername() throws Exception {
        String username = "soulscammer";
        Optional<User> user = Optional.of(UserFixture.getUser());
        UserPrincipalDto dto = new UserPrincipalDto();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(dtoMapper.map(user.get())).thenReturn(dto);

        assertThat(userService.findByUsername(username, dtoMapper)).isSameAs(dto);
    }
}