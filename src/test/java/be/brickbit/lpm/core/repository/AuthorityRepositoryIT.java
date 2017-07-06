package be.brickbit.lpm.core.repository;

import com.mysema.query.jpa.impl.JPAQuery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.QAuthority;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityRepositoryIT extends AbstractRepoIT {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void testFindByAuthority() throws Exception {
        String role_admin = "ROLE_ADMIN";
        Optional<Authority> authority = authorityRepository.findByAuthority(role_admin);

        assertThat(authority.isPresent());
        assertThat(authority.get()).isEqualTo(getAuthority(role_admin));
    }

    private Authority getAuthority(String roleName) {
        QAuthority authority = QAuthority.authority1;
        return new JPAQuery(getEntityManager())
                .from(authority)
                .where(authority.authority.eq(roleName))
                .uniqueResult(authority);
    }
}