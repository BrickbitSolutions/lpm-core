package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.domain.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    ActivationToken findByToken(String token);
}
