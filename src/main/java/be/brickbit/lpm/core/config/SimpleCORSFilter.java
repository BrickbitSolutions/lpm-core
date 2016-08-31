package be.brickbit.lpm.core.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {
    @Override
    public void init(FilterConfig someFilterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest someServletRequest, ServletResponse someServletResponse,
                         FilterChain someFilterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) someServletResponse;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,Origin,"
                + "Access-Control-Request-Method,Access-Control-Request-Headers,Authorization, Accept");
        someFilterChain.doFilter(someServletRequest, someServletResponse);
    }

    @Override
    public void destroy() {
    }
}
