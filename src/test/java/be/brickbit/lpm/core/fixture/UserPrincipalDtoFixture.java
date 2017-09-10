package be.brickbit.lpm.core.fixture;

import com.google.common.collect.Lists;

import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

public class UserPrincipalDtoFixture {
    public static UserPrincipalDto mutable() {
        return new UserPrincipalDto(
                randomLong(),
                randomString(),
                randomString(),
                randomString(),
                Lists.newArrayList(randomString())
        );
    }
}
