package be.brickbit.lpm.core.service.mapper;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.dto.UserDetailsDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
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
        User user = UserFixture.getUser();
        user.setBirthDate(LocalDate.now().minusYears(18));

        UserDetailsDto result = mapper.map(user);

        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getMood()).isEqualTo(user.getMood());
        assertThat(result.getWallet()).isEqualTo(user.getWallet());
        assertThat(result.getAge()).isEqualTo(18);
    }

}