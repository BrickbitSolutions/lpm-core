package be.brickbit.lpm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.controller.dto.UserProfileDto;
import be.brickbit.lpm.core.controller.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.controller.mapper.UserPrincipalDtoMapper;
import be.brickbit.lpm.core.controller.mapper.UserProfileDtoMapper;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.AbstractController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
public class UserController extends AbstractController {
    private final UserService userService;
    private final UserPrincipalDtoMapper userPrincipalDtoMapper;
    private final UserDetailsDtoMapper userDetailsDtoMapper;
    private final UserProfileDtoMapper userProfileDtoMapper;

    @Autowired
    public UserController(UserService userService, UserPrincipalDtoMapper userPrincipalDtoMapper, UserDetailsDtoMapper userDetailsDtoMapper, UserProfileDtoMapper userProfileDtoMapper) {
        this.userService = userService;
        this.userPrincipalDtoMapper = userPrincipalDtoMapper;
        this.userDetailsDtoMapper = userDetailsDtoMapper;
        this.userProfileDtoMapper = userProfileDtoMapper;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserPrincipalDto getCurrentUserDetails() {
        return userPrincipalDtoMapper.map(userService.findByUsername(getAuthenticatedUsername()));
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto getCurrentUserProfile() {
        return userProfileDtoMapper.map(userService.findByUsername(getAuthenticatedUsername()));
    }

    @RequestMapping(value = "profile", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict({"userDetailsBySeat"})
    public void updateUserProfile(@RequestBody @Valid UpdateUserProfileCommand command) {
        userService.updateUserProfile(getAuthenticatedUser().getId(), command);
    }

    @RequestMapping(value = "password", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(@RequestBody @Valid UpdateUserPasswordCommand command) {
        userService.updateUserPassword(getAuthenticatedUser().getId(), command);
    }

    @RequestMapping(value = "email", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserEmail(@RequestBody @Valid UpdateUserEmailCommand command) {
        userService.updateUserEmail(getAuthenticatedUser().getId(), command);
    }

    @RequestMapping(value = "seat/{seatNumber}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Cacheable("userDetailsBySeat")
    public UserDetailsDto getUserDetailsBySeat(@PathVariable("seatNumber") Integer seatNumber) {
        return userDetailsDtoMapper.map(userService.findBySeatNumber(seatNumber));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetails(@PathVariable("id") Long id) {
        return userDetailsDtoMapper.map(userService.findOne(id));
    }
}
