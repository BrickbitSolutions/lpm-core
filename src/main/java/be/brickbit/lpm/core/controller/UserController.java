package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.service.IUserService;
import be.brickbit.lpm.core.service.dto.UserDetailsDto;
import be.brickbit.lpm.core.service.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.service.mapper.UserPrincipalDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserPrincipalDtoMapper userPrincipalDtoMapper;

    @Autowired
    private UserDetailsDtoMapper userDetailsDtoMapper;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "principal", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserPrincipalDto getUserPrincipal(){
        return userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                userPrincipalDtoMapper
        );
    }

    @PreAuthorize(value = "hasAnyRole('SUPER_ADMIN', 'CATERING_ADMIN', 'CATERING_CREW')")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getUserDetails(@PathVariable("id") Long id){
        return userService.findByUserId(
                id,
                userDetailsDtoMapper
        );
    }
}
