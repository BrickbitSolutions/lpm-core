package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.controller.command.user.AssignSeatCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.controller.mapper.AdminUserDetailsDtoMapper;
import be.brickbit.lpm.core.controller.mapper.AuthorityNameMapper;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("admin/user")
public class UserAdminController extends AbstractController{
    private final UserService userService;
    private final AdminUserDetailsDtoMapper adminUserDetailsDtoMapper;
    private final AuthorityService authorityService;
    private final AuthorityNameMapper authorityNameMapper;

    @Autowired
    public UserAdminController(UserService userService, AdminUserDetailsDtoMapper adminUserDetailsDtoMapper, AuthorityService authorityService, AuthorityNameMapper authorityNameMapper) {
        this.userService = userService;
        this.adminUserDetailsDtoMapper = adminUserDetailsDtoMapper;
        this.authorityService = authorityService;
        this.authorityNameMapper = authorityNameMapper;
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

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/password", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable("id") Long id){
        userService.resetPassword(id);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "{id}/authorities", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict({"userDetailsBySeat"})
    public void updateUserAuthorities(@PathVariable("id") Long id, @RequestBody @Valid UpdateAuthoritiesCommand command) {
        userService.updateAuthorities(
                id,
                command
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Cacheable("authorities")
    public List<String> findAllAuthorities() {
        return authorityService.findAll(authorityNameMapper);
    }
}
