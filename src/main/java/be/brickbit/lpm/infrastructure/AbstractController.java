package be.brickbit.lpm.infrastructure;

import be.brickbit.lpm.core.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {
    protected User getCurrentUser(){
        return(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
