package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.QAuthority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import com.mysema.query.jpa.impl.JPAQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryIT extends AbstractRepoIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() throws Exception {
        User user = UserFixture.mutable();
        Authority role_user = getAuthority("ROLE_USER");
        user.getAuthorities().add(role_user);
        insert(user);

        Optional<User> result = userRepository.findByUsername(user.getUsername());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(result.get().getAuthorities()).containsExactly(role_user);
    }

    @Test
    public void testFindByEmail() throws Exception {
        User user = UserFixture.mutable();
        Authority role_user = getAuthority("ROLE_USER");
        user.getAuthorities().add(role_user);
        insert(user);

        Optional<User> result = userRepository.findByEmail(user.getEmail());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.get().getAuthorities()).containsExactly(role_user);

    }

    private Authority getAuthority(String roleName) {
        QAuthority authority = QAuthority.authority1;
        return new JPAQuery(getEntityManager())
                .from(authority)
                .where(authority.authority.eq(roleName))
                .uniqueResult(authority);
    }
}