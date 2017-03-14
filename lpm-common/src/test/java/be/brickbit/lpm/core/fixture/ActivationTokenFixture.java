package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.domain.ActivationToken;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

public class ActivationTokenFixture {
    public static ActivationToken mutable() {
        ActivationToken token = new ActivationToken();

        token.setToken(randomString());
        token.setUser(UserFixture.mutable());

        return token;
    }
}
