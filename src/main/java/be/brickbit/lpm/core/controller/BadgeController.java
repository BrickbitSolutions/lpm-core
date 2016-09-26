package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.controller.command.NewBadgeCommand;
import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.controller.mapper.BadgeDtoMapperImpl;
import be.brickbit.lpm.core.service.api.badge.BadgeService;
import be.brickbit.lpm.infrastructure.AbstractController;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user/badge")
public class BadgeController extends AbstractController {
    private final BadgeService badgeService;
    private final BadgeDtoMapperImpl badgeDtoMapper;

    @Autowired
    public BadgeController(BadgeService badgeService, BadgeDtoMapperImpl badgeDtoMapper) {
        this.badgeService = badgeService;
        this.badgeDtoMapper = badgeDtoMapper;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BadgeDto> getBadges(@RequestParam(value = "userId") Long userId) {
        return badgeService.findAllBadges(userId, badgeDtoMapper);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void invalidateBadge(@RequestParam(value = "token", required = false) String token,
                                @RequestParam(value = "userId", required = false) Long userId) {
        if (StringUtils.isNotBlank(token)) {
            badgeService.invalidateBadge(token);
        }

        if (userId != null) {
            badgeService.invalidateBadges(userId);
        }

        if (StringUtils.isBlank(token) && userId == null) {
            throw new ServiceException("No token or userId provided");
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BadgeDto createBadge(@RequestBody @Valid NewBadgeCommand command) {
        String uuid = command.getToken();

        if (StringUtils.isBlank(uuid)) {
            uuid = UUID.randomUUID().toString();
        }

        badgeService.createNewBadge(uuid, command.getUserId());

        return new BadgeDto(uuid, true);
    }
}
