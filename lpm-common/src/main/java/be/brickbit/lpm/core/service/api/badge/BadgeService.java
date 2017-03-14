package be.brickbit.lpm.core.service.api.badge;

import be.brickbit.lpm.core.service.api.user.UserDtoMapper;

import java.util.List;

public interface BadgeService {
    void createNewBadge(String token, Long userId);

    void invalidateBadges(Long userId);

    void invalidateBadge(String token);

    <T> List<T> findAllBadges(Long userId, BadgeDtoMapper<T> dtoMapper);

    <T> T findAssociatedUser(String token, UserDtoMapper<T> dtoMapper);
}
