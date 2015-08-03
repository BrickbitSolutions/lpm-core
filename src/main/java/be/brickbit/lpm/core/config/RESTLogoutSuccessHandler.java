package be.brickbit.lpm.core.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RESTLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest someHttpServletRequest, HttpServletResponse someHttpServletResponse, Authentication someAuthentication) throws IOException, ServletException {
        someHttpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
