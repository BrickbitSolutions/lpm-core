package be.brickbit.lpm.core.controller;

import com.mysema.query.jpa.impl.JPAQuery;

import org.junit.Test;

import java.util.List;
import java.util.UUID;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.controller.command.NewBadgeCommand;
import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.QBadge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.BadgeFixture;
import be.brickbit.lpm.core.fixture.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BadgeControllerIT extends AbstractControllerIT {
    @Test
    public void getsAllUserBadges() throws Exception {
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable(user);

        insert(
                user,
                badge
        );

        performGet("/user/badge?userId=" + user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].token", is(badge.getToken())))
                .andExpect(jsonPath("$[0].enabled", is(true)));
    }

    @Test
    public void invalidatesAllBadgeByUserId() throws Exception {
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable(user);
        badge.setEnabled(true);
        Badge badge2 = BadgeFixture.mutable(user);
        badge.setEnabled(true);

        insert(
                user,
                badge,
                badge2
        );

        performDelete("/user/badge?userId=" + user.getId())
                .andExpect(status().isNoContent());


        List<Badge> result = new JPAQuery(getEntityManager())
                .from(QBadge.badge)
                .where(QBadge.badge.user.eq(user))
                .list(QBadge.badge);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEnabled()).isFalse();
        assertThat(result.get(1).getEnabled()).isFalse();
    }

    @Test
    public void invalidateBadgeTriggersExceptionWhenNoParametersAreGiven() throws Exception {
        performDelete("/user/badge")
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidatesBadgeByToken() throws Exception {
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable(user);
        badge.setEnabled(true);

        insert(
                user,
                badge
        );

        performDelete("/user/badge?token=" + badge.getToken())
                .andExpect(status().isNoContent());


        Badge result = new JPAQuery(getEntityManager())
                .from(QBadge.badge)
                .where(QBadge.badge.token.eq(badge.getToken()))
                .uniqueResult(QBadge.badge);

        assertThat(result.getEnabled()).isFalse();
    }

    @Test
    public void createsBadgeAcceptsGivenToken() throws Exception {
        User user = UserFixture.mutable();
        String token = UUID.randomUUID().toString();

        insert(user);

        NewBadgeCommand command = new NewBadgeCommand(user.getId(), token);

        performPost("/user/badge", command)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(token)))
                .andExpect(jsonPath("$.enabled", is(true)));

        Badge result = new JPAQuery(getEntityManager())
                .from(QBadge.badge)
                .where(QBadge.badge.user.eq(user))
                .uniqueResult(QBadge.badge);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(token);
    }

    @Test
    public void createsBadgeGeneratesToken() throws Exception {
        User user = UserFixture.mutable();

        insert(user);

        NewBadgeCommand command = new NewBadgeCommand(user.getId(), null);

        performPost("/user/badge", command)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.enabled", is(true)));

        Badge result = new JPAQuery(getEntityManager())
                .from(QBadge.badge)
                .where(QBadge.badge.user.eq(user))
                .uniqueResult(QBadge.badge);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isNotNull();
    }

    @Test
    public void findsAssociatedUser() throws Exception {
        Badge badge = BadgeFixture.mutable();

        insert(
                badge.getUser(),
                badge
        );

        performGet("/user/badge/" + badge.getToken())
                .andExpect(status().isOk());
    }
}