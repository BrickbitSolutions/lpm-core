package be.brickbit.lpm.infrastructure;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.infrastructure.exception.SecurityException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {
    protected String getAuthenticatedUsername() {
        if (SecurityContextHolder.getContext() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            } else {
                throw new SecurityException("User not authenticated");
            }
        } else {
            throw new SecurityException("No SecurityContext found.");
        }
    }

    protected User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
