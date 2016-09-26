package be.brickbit.lpm.core.config;

import be.brickbit.lpm.core.service.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GlobalAuthenticationManagerConfig extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
