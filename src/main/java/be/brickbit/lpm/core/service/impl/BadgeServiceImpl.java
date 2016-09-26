package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.service.api.badge.BadgeDtoMapper;
import be.brickbit.lpm.core.service.api.badge.BadgeService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalBadgeService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private final InternalBadgeService internalBadgeService;
    private final InternalUserService internalUserService;

    @Autowired
    public BadgeServiceImpl(InternalBadgeService internalBadgeService, InternalUserService internalUserService) {
        this.internalBadgeService = internalBadgeService;
        this.internalUserService = internalUserService;
    }

    @Override
    public void createNewBadge(String token, Long userId) {
        Badge badge = new Badge();
        badge.setToken(token);
        badge.setUser(internalUserService.findOne(userId));
        badge.setEnabled(true);

        internalBadgeService.save(badge);
    }

    @Override
    public void invalidateBadges(Long userId) {
        List<Badge> badges = internalBadgeService.findAllByUserAndEnabledTrue(internalUserService.findOne(userId));

        badges.forEach(b -> b.setEnabled(false));
    }

    @Override
    public void invalidateBadge(String token) {
        Badge badge = internalBadgeService.findByToken(token);
        badge.setEnabled(false);
    }

    @Override
    public <T> List<T> findAllBadges(Long userId, BadgeDtoMapper<T> dtoMapper) {
        return internalBadgeService.findAllByUser(internalUserService.findOne(userId)).stream().map(dtoMapper::map).collect
                (Collectors
                        .toList());
    }
}
