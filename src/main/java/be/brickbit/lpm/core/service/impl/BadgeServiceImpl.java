package be.brickbit.lpm.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.BadgeRepository;
import be.brickbit.lpm.core.service.api.badge.BadgeService;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;

@Service
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private final UserService userService;
    private final BadgeRepository badgeRepository;

    @Autowired
    public BadgeServiceImpl(UserService userService, BadgeRepository badgeRepository) {
        this.userService = userService;
        this.badgeRepository = badgeRepository;
    }

    @Override
    public void createNewBadge(String token, Long userId) {
        Badge badge = new Badge();
        badge.setToken(token);
        badge.setUser(userService.findOne(userId));
        badge.setEnabled(true);

        badgeRepository.save(badge);
    }

    @Override
    public void invalidateBadges(Long userId) {
        List<Badge> badges = badgeRepository.findAllByUserAndEnabledTrue(userService.findOne(userId));

        badges.forEach(b -> b.setEnabled(false));
    }

    @Override
    public void invalidateBadge(String token) {
        Badge badge = findByToken(token);
        badge.setEnabled(false);
    }

    @Override
    public List<Badge> findAllBadges(Long userId) {
        return badgeRepository.findAllByUser(userService.findOne(userId));
    }

    @Override
    public Badge findByToken(String token) {
        return badgeRepository.findByToken(token).orElseThrow(() -> new EntityNotFoundException(String.format("Badge '%s' not found.", token)));
    }

    @Override
    public User findAssociatedUser(String token) {
        Badge badge = findByToken(token);

        if (badge.getEnabled()) {
            return badge.getUser();
        } else {
            throw new ServiceException(String.format("Badge '%s' is disabled", token));
        }
    }
}
