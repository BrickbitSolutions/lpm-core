package be.brickbit.lpm.core.service.api.authority;

import java.util.List;

public interface AuthorityService {
    <T> List<T> findAll(AuthorityDtoMapper<T> dtoMapper);
}
