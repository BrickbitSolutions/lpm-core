package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.Authority;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseSetup(
        value = "classpath:db-unit/authority.xml",
        type = DatabaseOperation.CLEAN_INSERT
)
public class AuthorityRepositoryTest extends AbstractRepoIT{

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void testFindByAuthority() throws Exception {
        String role_admin = "ROLE_ADMIN";
        Authority authority = authorityRepository.findByAuthority(role_admin);
        assertThat(authority).isNotNull();
        assertThat(authority.getAuthority()).isEqualTo(role_admin);
    }
}