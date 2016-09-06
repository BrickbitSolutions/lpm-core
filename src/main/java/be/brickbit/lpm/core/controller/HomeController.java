package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public String index() throws IOException {
        return "{\"status\":\"UP\"}";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void postRegisterForm(@RequestBody @Valid NewUserCommand newUserCommand) {
        userService.createUser(newUserCommand);
    }
}
