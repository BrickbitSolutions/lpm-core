package be.brickbit.lpm.infrastructure;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import be.brickbit.lpm.core.domain.User;

@RestController
public abstract class AbstractController {
    protected User getCurrentUser(){
        return(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
