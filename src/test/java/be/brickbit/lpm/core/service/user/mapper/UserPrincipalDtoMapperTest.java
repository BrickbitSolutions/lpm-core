package be.brickbit.lpm.core.service.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;

public class UserPrincipalDtoMapperTest {

    private UserPrincipalDtoMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new UserPrincipalDtoMapper();
    }

    @Test
    public void testMap() throws Exception {
        User user = UserFixture.mutable();
        UserPrincipalDto result = mapper.map(user);

        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getMood()).isEqualTo(user.getMood());
        assertThat(result.getAuthorities()).hasSize(user.getAuthorities().size());
    }
}