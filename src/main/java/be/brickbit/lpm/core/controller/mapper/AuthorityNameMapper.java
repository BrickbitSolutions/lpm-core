package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.service.api.authority.AuthorityDtoMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorityNameMapper implements AuthorityDtoMapper<String> {
    @Override
    public String map(Authority authority) {
        return authority.getAuthority();
    }
}
