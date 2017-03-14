package be.brickbit.lpm.core.service.impl.internal.impl;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.BadgeFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.repository.BadgeRepository;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InternalBadgeServiceImplTest {
    @Mock
    private BadgeRepository badgeRepository;

    @InjectMocks
    private InternalBadgeServiceImpl internalBadgeService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void savesBadge() throws Exception {
        Badge badge = BadgeFixture.mutable();

        internalBadgeService.save(badge);

        verify(badgeRepository, times(1)).save(badge);
    }

    @Test
    public void findsAllEnabledBadgesForUser() throws Exception {
        Badge badge = BadgeFixture.mutable();
        User user = UserFixture.mutable();

        when(badgeRepository.findAllByUserAndEnabledTrue(user)).thenReturn(Lists.newArrayList(badge));

        List<Badge> result = internalBadgeService.findAllByUserAndEnabledTrue(user);

        assertThat(result).containsExactly(badge);
    }

    @Test
    public void findsBadgeByToken() throws Exception {
        Badge badge = BadgeFixture.mutable();
        String token = randomString();

        when(badgeRepository.findByToken(token)).thenReturn(Optional.of(badge));

        Badge result = internalBadgeService.findByToken(token);

        assertThat(result).isSameAs(badge);
    }

    @Test
    public void throwsExceptionsWhenTokenDoesNotHaveBadge() throws Exception {
        String token = randomString();

        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("Badge '%s' not found.", token));

        when(badgeRepository.findByToken(token)).thenReturn(Optional.empty());

        internalBadgeService.findByToken(token);
    }

    @Test
    public void findsAllBadgesForUser() throws Exception {
        Badge badge = BadgeFixture.mutable();
        User user = UserFixture.mutable();

        when(badgeRepository.findAllByUser(user)).thenReturn(Lists.newArrayList(badge));

        List<Badge> result = internalBadgeService.findAllByUser(user);

        assertThat(result).containsExactly(badge);
    }
}