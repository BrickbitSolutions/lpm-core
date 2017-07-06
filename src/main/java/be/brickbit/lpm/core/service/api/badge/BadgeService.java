package be.brickbit.lpm.core.service.api.badge;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.user.UserDtoMapper;

import java.util.List;

public interface BadgeService {
    void createNewBadge(String token, Long userId);

    void invalidateBadges(Long userId);

    void invalidateBadge(String token);

    List<Badge> findAllBadges(Long userId);

    Badge findByToken(String token);

    User findAssociatedUser(String token);
}
