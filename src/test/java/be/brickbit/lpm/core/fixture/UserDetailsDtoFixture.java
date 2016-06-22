package be.brickbit.lpm.core.fixture;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomDecimal;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;

public class UserDetailsDtoFixture {
    public static UserDetailsDto mutable() {
        return new UserDetailsDto(
                randomString(),
                randomLong(),
                randomDecimal(),
                randomString(),
                randomString(),
                randomString(),
                randomString()
        );
    }
}
