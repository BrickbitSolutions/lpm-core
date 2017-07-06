package be.brickbit.lpm.core.service.api.authority;

import be.brickbit.lpm.core.domain.Authority;

import java.util.List;

public interface AuthorityService {
    Authority findByAuthority(String name);

    List<Authority> findAll();
}
