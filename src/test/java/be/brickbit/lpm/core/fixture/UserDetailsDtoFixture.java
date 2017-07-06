package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.controller.dto.UserDetailsDto;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomInt;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

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
