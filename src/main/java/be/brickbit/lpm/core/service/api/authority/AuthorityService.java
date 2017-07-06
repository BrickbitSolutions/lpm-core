package be.brickbit.lpm.core.service.api.authority;

import java.util.List;

import be.brickbit.lpm.core.domain.Authority;

public interface AuthorityService {
    Authority findByAuthority(String name);

    List<Authority> findAll();
}
