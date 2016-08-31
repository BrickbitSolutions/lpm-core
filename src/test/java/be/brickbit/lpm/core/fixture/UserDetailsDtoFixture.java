package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

public class UserDetailsDtoFixture {
    public static UserDetailsDto mutable() {
        return new UserDetailsDto(
                randomString(),
                randomLong(),
                randomInt(),
                randomDecimal(),
                randomString(),
                randomString(),
                randomString(),
                randomString()
        );
    }
}
