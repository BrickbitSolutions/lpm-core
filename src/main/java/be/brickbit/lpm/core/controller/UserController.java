package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.controller.command.user.AssignSeatCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.controller.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.controller.dto.UserDetailsDto;
import be.brickbit.lpm.core.controller.dto.UserPrincipalDto;
import be.brickbit.lpm.core.controller.mapper.AdminUserDetailsDtoMapper;
import be.brickbit.lpm.core.controller.mapper.AuthorityNameMapper;
import be.brickbit.lpm.core.controller.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.controller.mapper.UserPrincipalDtoMapper;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
public class UserController extends AbstractController {
    private final UserService userService;
    private final AuthorityService authorityService;
    private final AuthorityNameMapper authorityNameMapper;
    private final UserPrincipalDtoMapper userPrincipalDtoMapper;
    private final UserDetailsDtoMapper userDetailsDtoMapper;
    private final AdminUserDetailsDtoMapper adminUserDetailsDtoMapper;

    @Autowired
    public UserController(UserService userService, AuthorityService authorityService, AuthorityNameMapper authorityNameMapper, UserPrincipalDtoMapper userPrincipalDtoMapper, AdminUserDetailsDtoMapper adminUserDetailsDtoMapper, UserDetailsDtoMapper userDetailsDtoMapper) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.authorityNameMapper = authorityNameMapper;
        this.userPrincipalDtoMapper = userPrincipalDtoMapper;
        this.adminUserDetailsDtoMapper = adminUserDetailsDtoMapper;
        this.userDetailsDtoMapper = userDetailsDtoMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> findAllAuthorities() {
        return authorityService.findAll(authorityNameMapper);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "me", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserPrincipalDto getCurrentUserDetails() {
        return userService.findByUsername(
                getAuthenticatedUsername(),
                userPrincipalDtoMapper
        );
    }

    @PreAuthorize(value = "isAuthenticated()")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetails(@PathVariable("id") Long id) {
        return userService.findOne(
                id,
                userDetailsDtoMapper
        );
    }

    @PreAuthorize(value = "isAuthenticated()")
    @RequestMapping(value = "/seat/{seatNumber}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetailsBySeat(@PathVariable("seatNumber") Integer seatNumber) {
        return userService.findBySeatNumber(
                seatNumber,
                userDetailsDtoMapper
        );
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/details", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AdminUserDetailsDto getAdminUserDetails(@PathVariable("id") Long id) {
        return userService.findOne(id, adminUserDetailsDtoMapper);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/seat", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignSeatNumber(@PathVariable("id") Long id, @RequestBody AssignSeatCommand command) {
        userService.assignSeat(id, command.getSeatNumber());
    }

    @PreAuthorize(value = "isAuthenticated()")
    @RequestMapping(value = "/profile", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserProfile(@RequestBody @Valid UpdateUserProfileCommand command) {
        userService.updateUserProfile(getAuthenticatedUser().getId(), command);
    }

    @PreAuthorize(value = "isAuthenticated()")
    @RequestMapping(value = "/password", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(@RequestBody @Valid UpdateUserPasswordCommand command) {
        userService.updateUserPassword(getAuthenticatedUser().getId(), command);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable("id") Long id, @RequestBody @Valid UpdateAccountDetailsCommand command) {
        userService.updateAccountDetails(
                id,
                command
        );
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<AdminUserDetailsDto> getAllUsers() {
        return userService.findAll(adminUserDetailsDtoMapper);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/enable", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable("id") Long id) {
        userService.enableUser(id);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(@PathVariable("id") Long id) {
        userService.disableUser(id);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/lock", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lockUser(@PathVariable("id") Long id) {
        userService.lockUser(id);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/unlock", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockUser(@PathVariable("id") Long id) {
        userService.unlockUser(id);
    }
}
