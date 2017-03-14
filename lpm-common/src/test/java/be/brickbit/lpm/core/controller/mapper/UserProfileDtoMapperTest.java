package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.UserProfileDto;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileDtoMapperTest {
    private final UserProfileDtoMapper userProfileDtoMapper;

    public UserProfileDtoMapperTest() {
        userProfileDtoMapper = new UserProfileDtoMapper();
    }

    @Test
    public void map() throws Exception {
        User user = UserFixture.mutable();

        UserProfileDto result = userProfileDtoMapper.map(user);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getMood()).isEqualTo(user.getMood());
        assertThat(result.getSeatNumber()).isEqualTo(user.getSeatNumber());
        assertThat(result.getBirthDate()).isEqualTo(user.getBirthDate().format(DateTimeFormatter.ISO_DATE));
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getMobilePhoneNr()).isEqualTo(user.getMobileNr());
    }
}