package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;

import java.util.UUID;

public class BadgeFixture {
    public static Badge mutable() {
        return mutable(UserFixture.mutable());
    }

    public static Badge mutable(User user) {
        Badge badge = new Badge();
        badge.setToken(UUID.randomUUID().toString());
        badge.setUser(user);
        badge.setEnabled(true);

        return badge;
    }
}
