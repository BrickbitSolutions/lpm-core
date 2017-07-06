package be.brickbit.lpm.core.service.impl;

import com.google.common.collect.Lists;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.BadgeFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.repository.BadgeRepository;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.exception.ServiceException;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BadgeServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private BadgeRepository badgeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private BadgeServiceImpl badgeService;
    @Captor
    private ArgumentCaptor<Badge> badgeArgumentCaptor;

    @Test
    public void createsNewBadge() throws Exception {
        final Long userId = randomLong();
        final User user = UserFixture.mutable();
        final String token = randomString();

        when(userService.findOne(userId)).thenReturn(user);

        badgeService.createNewBadge(token, userId);
        verify(badgeRepository, times(1)).save(badgeArgumentCaptor.capture());

        Badge savedBadge = badgeArgumentCaptor.getValue();

        assertThat(savedBadge.getUser()).isSameAs(user);
        assertThat(savedBadge.getToken()).isSameAs(token);
        assertThat(savedBadge.getEnabled()).isTrue();
    }

    @Test
    public void invalidatesAllUserBadges() throws Exception {
        Long userId = randomLong();
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable(user);
        badge.setEnabled(true);

        when(userService.findOne(userId)).thenReturn(user);
        when(badgeRepository.findAllByUserAndEnabledTrue(user)).thenReturn(Lists.newArrayList(badge));

        badgeService.invalidateBadges(userId);

        assertThat(badge.getEnabled()).isFalse();
    }

    @Test
    public void findsBadgeByToken() throws Exception {
        Badge badge = BadgeFixture.mutable();
        String token = randomString();

        when(badgeRepository.findByToken(token)).thenReturn(Optional.of(badge));

        Optional<Badge> result = badgeRepository.findByToken(token);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isSameAs(badge);
    }

    @Test
    public void invalidatesBadgeByToken() throws Exception {
        Badge badge = BadgeFixture.mutable();
        badge.setEnabled(true);

        when(badgeRepository.findByToken(badge.getToken())).thenReturn(Optional.of(badge));

        badgeService.invalidateBadge(badge.getToken());

        assertThat(badge.getEnabled()).isFalse();
    }

    @Test
    public void findsAllBadgesForUser() throws Exception {
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable();
        Long userId = randomLong();

        when(userService.findOne(userId)).thenReturn(user);
        when(badgeRepository.findAllByUser(user)).thenReturn(Lists.newArrayList(badge));

        List<Badge> result = badgeService.findAllBadges(userId);

        assertThat(result).containsExactly(badge);
    }

    @Test
    public void findsAssociatedUser() throws Exception {
        Badge badge = BadgeFixture.mutable();

        when(badgeRepository.findByToken(badge.getToken())).thenReturn(Optional.of(badge));

        User result = badgeService.findAssociatedUser(badge.getToken());

        assertThat(result).isSameAs(badge.getUser());
    }

    @Test
    public void shouldThrownServiceExceptionWhenBadgeIsDisabled() throws Exception {
        Badge badge = BadgeFixture.mutable();
        badge.setEnabled(false);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(String.format("Badge '%s' is disabled", badge.getToken()));

        when(badgeRepository.findByToken(badge.getToken())).thenReturn(Optional.of(badge));

        badgeService.findAssociatedUser(badge.getToken());
    }
}