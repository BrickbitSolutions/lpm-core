package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.BadgeFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BadgeRepositoryIT extends AbstractRepoIT {

    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void findsAllBadgesByUser() throws Exception {
        User user = UserFixture.mutable();
        User otherUser = UserFixture.mutable();

        Badge badge1 = BadgeFixture.mutable(user);
        Badge badge2 = BadgeFixture.mutable(user);

        insert(
                user,
                otherUser,
                badge1,
                badge2,
                BadgeFixture.mutable(otherUser)
        );

        List<Badge> result = badgeRepository.findAllByUser(user);

        assertThat(result).containsExactly(badge1, badge2);
    }

    @Test
    public void findsAllEnabledBadgesByUser() throws Exception {
        User user = UserFixture.mutable();

        Badge badge1 = BadgeFixture.mutable(user);
        Badge badge2 = BadgeFixture.mutable(user);
        badge2.setEnabled(false);

        insert(
                user,
                badge1,
                badge2
        );

        List<Badge> result = badgeRepository.findAllByUserAndEnabledTrue(user);

        assertThat(result).containsExactly(badge1);
    }

    @Test
    public void findsBadgeByToken() throws Exception {
        Badge badge = BadgeFixture.mutable();

        insert(
                badge.getUser(),
                badge
        );

        Optional<Badge> result = badgeRepository.findByToken(badge.getToken());

        assertThat(result).isEqualTo(Optional.of(badge));
    }

}