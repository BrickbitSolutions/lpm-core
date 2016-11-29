package be.brickbit.lpm.core.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(value = "me", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserPrincipalDto getCurrentUserDetails() {
        return userService.findByUsername(
                getAuthenticatedUsername(),
                userPrincipalDtoMapper
        );
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto getCurrentUserProfile(){
        return userService.findByUsername(
                getAuthenticatedUsername(),
                userProfileDtoMapper
        );
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
        return userService.findBySeatNumber(
                seatNumber,
                userDetailsDtoMapper
        );
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetails(@PathVariable("id") Long id) {
        return userService.findOne(
                id,
                userDetailsDtoMapper
        );
    }
}
