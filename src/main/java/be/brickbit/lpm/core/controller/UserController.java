package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.service.security.SecurityService;
import be.brickbit.lpm.core.service.user.UserService;
import be.brickbit.lpm.core.service.user.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.user.mapper.AdminUserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserPrincipalDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserPrincipalDtoMapper userPrincipalDtoMapper;

    @Autowired
    private UserDetailsDtoMapper userDetailsDtoMapper;

    @Autowired
    private AdminUserDetailsDtoMapper adminUserDetailsDtoMapper;

    @Autowired
    private SecurityService securityService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> findAllAuthorities() {
        return userService.findAllAuthorities();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "principal", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserPrincipalDto getUserPrincipal(){
        return userService.findByUsername(
                securityService.getAuthenticatedUsername(),
                userPrincipalDtoMapper
        );
    }

    @PreAuthorize(value = "isAuthenticated()")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetails(@PathVariable("id") Long id){
        return userService.findOne(
                id,
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
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable("id") Long id, @RequestBody UpdateAccountDetailsCommand command){
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

    @RequestMapping(value = "{id}/enable", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable("id") Long id){
        userService.enableUser(id);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(@PathVariable("id") Long id){
        userService.disableUser(id);
    }

    @RequestMapping(value = "{id}/lock", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lockUser(@PathVariable("id") Long id){
        userService.lockUser(id);
    }

    @RequestMapping(value = "{id}/unlock", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockUser(@PathVariable("id") Long id){
        userService.unlockUser(id);
    }
}
