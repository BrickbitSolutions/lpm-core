package be.brickbit.lpm.core.controller.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.domain.Badge;

@Component
public class BadgeDtoMapperImpl implements Converter<Badge, BadgeDto> {
    @Override
    public BadgeDto convert(Badge badge) {
        return new BadgeDto(
                badge.getToken(),
                badge.getEnabled()
        );
    }
}
