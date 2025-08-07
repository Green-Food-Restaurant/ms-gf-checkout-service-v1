package com.greenfood.checkout_service.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite requisições de origens específicas
        // Não podemos usar "*" com allowCredentials=true
        config.addAllowedOriginPattern("*");
        
        // Permite os métodos HTTP mais comuns
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");
        
        // Permite todos os headers
        config.addAllowedHeader("*");
        
        // Expõe headers específicos que podem ser acessados pela aplicação cliente
        config.addExposedHeader("Authorization");
        
        // Habilita credenciais
        config.setAllowCredentials(true);
        
        // Aplica esta configuração para todos os endpoints
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}