package be.brickbit.lpm.core.service.security.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import be.brickbit.lpm.core.auth.exceptions.SecurityException;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.security.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Override
    public String getAuthenticatedUsername() {
        if(SecurityContextHolder.getContext() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            } else {
                throw new SecurityException("User not authenticated");
            }
        }else{
            throw new SecurityException("No SecurityContext found.");
        }
    }

    @Override
    public User getAuthenticatedUser() {
        return(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
