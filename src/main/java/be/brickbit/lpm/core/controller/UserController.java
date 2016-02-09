package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.service.IUserService;
import be.brickbit.lpm.core.service.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.mapper.UserPrincipalDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserPrincipalDtoMapper userPrincipalDtoMapper;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "principal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserPrincipalDto getUserPrincipal(){
        return userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                userPrincipalDtoMapper
        );
    }
}
