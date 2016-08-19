package be.brickbit.lpm.core.service.security;

import be.brickbit.lpm.core.domain.User;

public interface SecurityService {
    String getAuthenticatedUsername();

    User getAuthenticatedUser();
}
