package be.brickbit.lpm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.brickbit.lpm.core.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findByAuthority(String authority);
}
