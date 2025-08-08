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
        // Permite origens específicas para produção e desenvolvimento
        config.addAllowedOrigin("https://greenfood.devarchitects.tech");
        config.addAllowedOrigin("https://checkout.greenfood.devarchitects.tech");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:3001");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://127.0.0.1:3000");
        
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
        config.addExposedHeader("Content-Type");
        
        // Habilita credenciais
        config.setAllowCredentials(true);
        
        // Cache preflight por 1 hora
        config.setMaxAge(3600L);
        
        // Aplica esta configuração para todos os endpoints
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}