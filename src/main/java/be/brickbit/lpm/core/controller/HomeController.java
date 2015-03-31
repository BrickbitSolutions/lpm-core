package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.auth.exceptions.UserExistsException;
import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "loginForm";
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterForm(@ModelAttribute NewUserCommand newUserCommand){
        return "registerForm";
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegisterForm(@ModelAttribute @Valid NewUserCommand newUserCommand, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "registerForm";
        }

        try {
            userService.createUser(newUserCommand);
        }catch (UserExistsException ex){
            bindingResult.addError(new ObjectError("newUserCommand", ex.getMessage()));
            return "registerForm";
        }

        return "redirect:/login";
    }
}
