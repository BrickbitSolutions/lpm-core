package be.brickbit.lpm.core.service.impl.internal.impl;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.repository.BadgeRepository;
import be.brickbit.lpm.core.service.impl.internal.api.InternalBadgeService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalBadgeServiceImpl implements InternalBadgeService {
    private final BadgeRepository badgeRepository;

    @Autowired
    public InternalBadgeServiceImpl(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @Override
    public void save(Badge badge) {
        badgeRepository.save(badge);
    }

    @Override
    public List<Badge> findAllByUserAndEnabledTrue(User user) {
        return badgeRepository.findAllByUserAndEnabledTrue(user);
    }

    @Override
    public Badge findByToken(String token) {
        return badgeRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException(String.format("Badge '%s' not found.", token))
        );
    }

    @Override
    public List<Badge> findAllByUser(User user) {
        return badgeRepository.findAllByUser(user);
    }
}
