package be.brickbit.lpm.core.controller.mapper;

import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.service.api.badge.BadgeDtoMapper;
import org.springframework.stereotype.Component;

@Component
public class BadgeDtoMapperImpl implements BadgeDtoMapper<BadgeDto> {
    @Override
    public BadgeDto map(Badge badge) {
        return new BadgeDto(
                badge.getToken(),
                badge.getEnabled()
        );
    }
}
