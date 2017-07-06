package be.brickbit.lpm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.validation.Valid;

import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.api.user.UserService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class HomeController {
    private final UserService userService;
    private final ConsumerTokenServices consumerTokenServices;

    @Autowired
    public HomeController(UserService userService, ConsumerTokenServices consumerTokenServices) {
        this.userService = userService;
        this.consumerTokenServices = consumerTokenServices;
    }

    @RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public String index() throws IOException {
        return "{\"status\":\"UP\"}";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void postRegisterForm(@RequestBody @Valid NewUserCommand newUserCommand) {
        userService.createUser(newUserCommand);
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable("token") String token) {
        userService.activateUser(token);
    }

    @RequestMapping(value = "/oauth/revoke", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestParam("token") String token) {
        consumerTokenServices.revokeToken(token);
    }
}
