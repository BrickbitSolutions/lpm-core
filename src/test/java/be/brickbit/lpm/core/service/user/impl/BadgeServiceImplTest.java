package be.brickbit.lpm.core.service.user.impl;

import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.controller.mapper.BadgeDtoMapperImpl;
import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.BadgeFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.service.impl.BadgeServiceImpl;
import be.brickbit.lpm.core.service.impl.internal.api.InternalBadgeService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BadgeServiceImplTest {

    @Mock
    private InternalBadgeService internalBadgeService;

    @Mock
    private InternalUserService internalUserService;

    @Mock
    private BadgeDtoMapperImpl dtoMapper;

    @InjectMocks
    private BadgeServiceImpl badgeService;

    @Captor
    private ArgumentCaptor<Badge> badgeArgumentCaptor;

    @Test
    public void createsNewBadge() throws Exception {
        final Long userId = randomLong();
        final User user = UserFixture.mutable();
        final String token = randomString();

        when(internalUserService.findOne(userId)).thenReturn(user);

        badgeService.createNewBadge(token, userId);
        verify(internalBadgeService, times(1)).save(badgeArgumentCaptor.capture());

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

        when(internalUserService.findOne(userId)).thenReturn(user);
        when(internalBadgeService.findAllByUserAndEnabledTrue(user)).thenReturn(Lists.newArrayList(badge));

        badgeService.invalidateBadges(userId);

        assertThat(badge.getEnabled()).isFalse();
    }

    @Test
    public void invalidatesBadgeByToken() throws Exception {
        Badge badge = BadgeFixture.mutable();
        badge.setEnabled(true);

        when(internalBadgeService.findByToken(badge.getToken())).thenReturn(badge);

        badgeService.invalidateBadge(badge.getToken());

        assertThat(badge.getEnabled()).isFalse();
    }

    @Test
    public void findsAllBadgesForUser() throws Exception {
        User user = UserFixture.mutable();
        Badge badge = BadgeFixture.mutable();
        BadgeDto badgeDto = new BadgeDto(badge.getToken(), badge.getEnabled());
        Long userId = randomLong();

        when(internalUserService.findOne(userId)).thenReturn(user);
        when(internalBadgeService.findAllByUser(user)).thenReturn(Lists.newArrayList(badge));
        when(dtoMapper.map(badge)).thenReturn(badgeDto);

        List<BadgeDto> result = badgeService.findAllBadges(userId, dtoMapper);

        assertThat(result).containsExactly(badgeDto);
    }
}