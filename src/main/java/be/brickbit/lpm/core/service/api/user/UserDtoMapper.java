package be.brickbit.lpm.core.service.api.user;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.infrastructure.mapper.Mapper;

public interface UserDtoMapper<T> extends Mapper<User, T> {
}
