package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.User;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseSetup(
        value = {
                "classpath:db-unit/authority.xml",
                "classpath:db-unit/user.xml",
                "classpath:db-unit/user_authority.xml"
        },
        type = DatabaseOperation.CLEAN_INSERT
)
public class UserRepositoryTest extends AbstractRepoIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() throws Exception {
        String username = "admin";
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }

    @Test
    public void testFindByEmail() throws Exception {
        String email = "jonas.liekens@brickbit.be";
        Optional<User> user = userRepository.findByEmail(email);

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getEmail()).isEqualTo(email);
    }
}