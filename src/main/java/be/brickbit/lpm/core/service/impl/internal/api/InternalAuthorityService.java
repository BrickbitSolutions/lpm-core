package be.brickbit.lpm.core.service.impl.internal.api;

import be.brickbit.lpm.core.domain.Authority;

import java.util.List;

public interface InternalAuthorityService {
    Authority findByAuthority(String name);

    List<Authority> findAll();
}
