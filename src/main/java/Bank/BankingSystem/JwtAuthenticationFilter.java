package Bank.BankingSystem;

import Bank.BankingSystem.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// This is a jwtauth Filter that stays in  top of userpassword auth filter to validate the jwt token if its already in the application.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        // we are extracting the bearer token from the token header, we substring from 7 because its
        //starts with word bearer which is 7 letter long.
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))){
            filterChain.doFilter(request, response);
            return;

        }
        jwtToken = jwtToken.startsWith("Bearer")? jwtToken.substring(7): jwtToken;

    }
}
