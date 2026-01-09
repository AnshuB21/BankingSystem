package Bank.BankingSystem.Config;
import Bank.BankingSystem.Repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.security.core.userdetails.UserDetailsService; import org.springframework.security.core.userdetails.UsernameNotFoundException; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration

public class AppConfig {


    private final UserRepository userRepository;

    public AppConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
/*AuthenticationManager routes to DaoAuthenticationProvider, which calls your UserDetailsService
(loads user from DB) and PasswordEncoder.matches(); if valid it returns an authenticated Authentication
 and Spring stores it in the SecurityContext (and in session if stateful). */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDetails user = userRepository.findByUsernameIgnoreCase(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return user;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoProvider =
                new DaoAuthenticationProvider(userDetailsService());
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
