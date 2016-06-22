package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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
