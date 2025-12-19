package Bank.BankingSystem.Config;

import Bank.BankingSystem.Repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    //with final and the RequiredArgsConstructor , the actual long code becomes
//    public AppConfig(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    private final UserRepository userRepository;
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
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider(userDetailsService());
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
