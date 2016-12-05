package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsDtoMapperTest {
    private UserDetailsDtoMapper mapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mapper = new UserDetailsDtoMapper();
    }

    @Test
    public void map() throws Exception {
        User user = UserFixture.mutable();
        user.setBirthDate(LocalDate.now().minusYears(18));

        UserDetailsDto result = mapper.map(user);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getMood()).isEqualTo(user.getMood());
        assertThat(result.getSeatNumber()).isEqualTo(user.getSeatNumber());
        assertThat(result.getAge()).isEqualTo(18);
    }

    @Test
    public void throwsExceptionOnNullUser() throws Exception {
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("User not found.");

        mapper.map(null);
    }
}