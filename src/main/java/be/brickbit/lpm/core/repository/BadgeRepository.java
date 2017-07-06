package be.brickbit.lpm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.domain.User;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByUser(User user);

    List<Badge> findAllByUserAndEnabledTrue(User user);

    Optional<Badge> findByToken(String token);
}
