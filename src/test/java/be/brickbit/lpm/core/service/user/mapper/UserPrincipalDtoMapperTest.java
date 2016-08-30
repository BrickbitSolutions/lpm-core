package be.brickbit.lpm.core.service.user.mapper;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;

import java.time.LocalDate;

public class UserPrincipalDtoMapperTest {

    private UserPrincipalDtoMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new UserPrincipalDtoMapper();
    }

    @Test
    public void testMap() throws Exception {
        User user = UserFixture.mutable();
        user.setId(randomLong());
        user.setBirthDate(LocalDate.now().minusYears(18));
        UserPrincipalDto result = mapper.map(user);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getMood()).isEqualTo(user.getMood());
        assertThat(result.getSeatNumber()).isEqualTo(user.getSeatNumber());
        assertThat(result.getWallet()).isEqualTo(user.getWallet());
        assertThat(result.getAge()).isEqualTo(18);
        assertThat(result.getAuthorities()).hasSize(user.getAuthorities().size());
    }
}