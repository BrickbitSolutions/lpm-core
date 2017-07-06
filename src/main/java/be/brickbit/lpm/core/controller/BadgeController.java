package be.brickbit.lpm.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import be.brickbit.lpm.core.controller.command.NewBadgeCommand;
import be.brickbit.lpm.core.controller.dto.BadgeDto;
import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.controller.mapper.BadgeDtoMapperImpl;
import be.brickbit.lpm.core.controller.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.service.api.badge.BadgeService;
import be.brickbit.lpm.infrastructure.AbstractController;
import be.brickbit.lpm.infrastructure.exception.ServiceException;

@RestController
@RequestMapping(value = "/user/badge")
public class BadgeController extends AbstractController {
    private final BadgeService badgeService;
    private final BadgeDtoMapperImpl badgeDtoMapper;
    private final UserDetailsDtoMapper userDetailsDtoMapper;

    @Autowired
    public BadgeController(BadgeService badgeService, BadgeDtoMapperImpl badgeDtoMapper, UserDetailsDtoMapper userDetailsDtoMapper) {
        this.badgeService = badgeService;
        this.badgeDtoMapper = badgeDtoMapper;
        this.userDetailsDtoMapper = userDetailsDtoMapper;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BadgeDto> getBadges(@RequestParam(value = "userId") Long userId) {
        return badgeService.findAllBadges(userId).stream().map(badgeDtoMapper::map).collect(Collectors.toList());
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

    @RequestMapping(value = "/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserByBadge(@PathVariable("token") String token) {
        return userDetailsDtoMapper.map(badgeService.findAssociatedUser(token));
    }
}
