package be.brickbit.lpm.core.controller.mapper;

import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.service.api.authority.AuthorityDtoMapper;

@Component
public class AuthorityNameMapper implements AuthorityDtoMapper<String> {
    @Override
    public String map(Authority authority) {
        return authority.getAuthority();
    }
}
