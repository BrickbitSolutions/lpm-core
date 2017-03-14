package be.brickbit.lpm.core.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class JwtTokenConfig implements JwtAccessTokenConverterConfigurer {
    @Override
    public void configure(JwtAccessTokenConverter converter) {
        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new LpmUserAuthenticationConverter());
        converter.setAccessTokenConverter(tokenConverter);
    }
}
