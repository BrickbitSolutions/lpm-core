package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsDtoMapperTest {
    private UserDetailsDtoMapper mapper;

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
}