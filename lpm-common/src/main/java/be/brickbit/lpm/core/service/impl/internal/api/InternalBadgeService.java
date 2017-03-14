package be.brickbit.lpm.core.service.impl.internal.api;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;

import java.util.List;

public interface InternalBadgeService {
    void save(Badge badge);

    List<Badge> findAllByUserAndEnabledTrue(User user);

    Badge findByToken(String token);

    List<Badge> findAllByUser(User user);
}
