package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import com.google.common.collect.Lists;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

public class UserPrincipalDtoFixture {
    public static UserPrincipalDto mutable() {
        return new UserPrincipalDto(
                randomLong(),
                randomString(),
                randomLong(),
                randomInt(),
                randomString(),
                randomString(),
                randomString(),
                randomString(),
                Lists.newArrayList(randomString())
        );
    }
}
