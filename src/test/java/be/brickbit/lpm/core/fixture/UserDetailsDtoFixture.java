package be.brickbit.lpm.core.fixture;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;

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
