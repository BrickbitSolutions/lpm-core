package be.brickbit.lpm.auth.service.impl;

import be.brickbit.lpm.auth.domain.User;
import be.brickbit.lpm.auth.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = "username";
        final User repoUser = new User();
        when(repository.findByUsername(username)).thenReturn(Optional.of(repoUser));

        User user = (User) userService.loadUserByUsername(username);

        assertThat(user).isSameAs(repoUser);
    }
}