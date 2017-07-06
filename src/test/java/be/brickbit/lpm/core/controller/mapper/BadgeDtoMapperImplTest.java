package be.brickbit.lpm.core.controller.mapper;

import org.junit.Before;
import org.junit.Test;

import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.domain.Badge;
import be.brickbit.lpm.core.fixture.BadgeFixture;

import static org.assertj.core.api.Assertions.assertThat;

public class BadgeDtoMapperImplTest {

    private BadgeDtoMapperImpl mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new BadgeDtoMapperImpl();
    }

    @Test
    public void mapsBadgeDto() throws Exception {
        Badge badge = BadgeFixture.mutable();

        BadgeDto result = mapper.map(badge);

        assertThat(result.getEnabled()).isEqualTo(badge.getEnabled());
        assertThat(result.getToken()).isEqualTo(badge.getToken());
    }
}