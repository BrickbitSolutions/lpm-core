package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityNameMapperTest {
    private AuthorityNameMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new AuthorityNameMapper();
    }

    @Test
    public void map() throws Exception {
        Authority authority = AuthorityFixture.user();
        assertThat(mapper.map(authority)).isEqualTo(authority.getAuthority());
    }

}