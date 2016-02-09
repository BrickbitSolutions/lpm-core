package be.brickbit.lpm.core.service.mapper;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.dto.UserPrincipalDto;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPrincipalDtoMapperTest {

    private UserPrincipalDtoMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new UserPrincipalDtoMapper();
    }

    @Test
    public void testMap() throws Exception {
        User user = UserFixture.getUser();
        UserPrincipalDto result = mapper.map(user);

        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getMood()).isEqualTo("I will rule the universe");
        assertThat(result.getAuthorities()).hasSize(user.getAuthorities().size());
    }
}