package Bank.BankingSystem.Config;

import Bank.BankingSystem.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private JwtAuthenticationFilter authFilter;
    private AuthenticationProvider authenticationProvider;
    public SecurityConfig(JwtAuthenticationFilter authFilter,
                          AuthenticationProvider authenticationProvider) {
        this.authFilter = authFilter;
        this.authenticationProvider = authenticationProvider;
    }


    //need to override the security filter chain by creating new bean for this filter chain
    @Bean // Register this method as the main Spring Security configuration
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Enable CORS so frontend (different origin) can call this backend
                .cors(Customizer.withDefaults())

                // Disable CSRF because this is a stateless JWT-based API (no sessions)
                .csrf(csrf -> csrf.disable())

                // Define authorization rules for HTTP requests
                .authorizeHttpRequests(auth -> auth


                        // Allow unauthenticated access to login & register endpoints
                        .requestMatchers("/user/auth", "/user/register").permitAll()

                        // Require authentication (valid JWT) for all other endpoints
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)

                // Do not create or use HTTP sessions; each request must carry its own JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Run JWT authentication filter before Spring's username/password filter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured security filter chain
        return http.build();
    }
//cors configuration is important

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
