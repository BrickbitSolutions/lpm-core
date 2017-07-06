package be.brickbit.lpm.core.fixture;

import java.util.UUID;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;

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
