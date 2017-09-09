package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.domain.Authority;

@Component
public class AuthorityNameMapper implements Converter<Authority, String> {

    @Override
    public String convert(Authority authority) {
        return authority.getAuthority();
    }
}
