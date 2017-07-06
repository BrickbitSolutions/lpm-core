package be.brickbit.lpm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.brickbit.lpm.core.domain.ActivationToken;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    ActivationToken findByToken(String token);
}
