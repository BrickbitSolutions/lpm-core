package be.brickbit.lpm.core.service.user.mapper;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.user.dto.AdminUserDetailsDto;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminUserDetailsDtoMapperTest {
    private AdminUserDetailsDtoMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new AdminUserDetailsDtoMapper();
    }

	@Test
	public void map() throws Exception {
        User user = UserFixture.mutable();

        AdminUserDetailsDto result = mapper.map(user);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getBirthDate()).isEqualTo(user.getBirthDate());
        assertThat(result.getEnabled()).isEqualTo(user.isEnabled());
        assertThat(result.getLocked()).isEqualTo(!user.isAccountNonLocked());
        assertThat(result.getAuthorities()).containsAll(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
	}
}