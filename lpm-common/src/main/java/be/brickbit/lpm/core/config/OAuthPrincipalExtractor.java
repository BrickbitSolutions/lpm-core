package be.brickbit.lpm.core.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Profile("test")
public class OAuthPrincipalExtractor implements PrincipalExtractor {
    @Override
    public LpmUserAuthenticationConverter.LpmTokenPrincipal extractPrincipal(Map<String, Object> map) {
        return new LpmUserAuthenticationConverter.LpmTokenPrincipal(
                extractLong(map, "id"),
                extractString(map, "username")
        );
    }

    private Integer extractInteger(Map<String, Object> map, String key){
        Object value = map.get(key);
        if(value != null){
            return Integer.valueOf(value.toString());
        }else{
            return null;
        }
    }

    private Long extractLong(Map<String, Object> map, String key){
        Object value = map.get(key);
        if(value != null){
            return Long.valueOf(value.toString());
        }else{
            return null;
        }
    }

    private String extractString(Map<String, Object> map, String key){
        Object value = map.get(key);
        if(value != null){
            return value.toString();
        }else{
            return null;
        }
    }

    private BigDecimal extractBigDecimal(Map<String, Object> map, String key){
        Object value = map.get(key);
        if(value != null){
            return BigDecimal.valueOf(Double.valueOf(value.toString()));
        }else{
            return null;
        }
    }

    private List extractList(Map<String, Object> map, String key){
        Object value = map.get(key);
        if(value != null){
            return (List) value;
        }else{
            return null;
        }
    }
}
