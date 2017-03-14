package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

public class UserDetailsDtoFixture {
    public static UserDetailsDto mutable() {
        return new UserDetailsDto(
                randomLong(),
                randomString(),
                randomLong(),
                randomInt(),
                randomString()
        );
    }
}
