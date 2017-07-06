package be.brickbit.lpm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import be.brickbit.lpm.core.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findBySeatNumber(Integer seatNumber);
}
