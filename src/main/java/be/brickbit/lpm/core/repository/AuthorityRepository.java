package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findByAuthority(String authority);
}
