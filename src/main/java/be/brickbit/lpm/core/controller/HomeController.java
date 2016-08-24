package be.brickbit.lpm.core.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.user.UserService;

@RestController
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index() throws IOException{
        return "{\"status\":\"UP\"}";
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void postRegisterForm(@RequestBody @Valid NewUserCommand newUserCommand) {
        userService.createUser(newUserCommand);
    }
}
