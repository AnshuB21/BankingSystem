package Bank.BankingSystem.filters;

import Bank.BankingSystem.Service.JwtService;
import Bank.BankingSystem.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// This is a jwtauth Filter that stays in  top of userpassword auth filter to validate the jwt token if its already in the application.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
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
        // Extract the username (subject) stored inside the JWT; this identifies *who* the token belongs to
        String subject= jwtService.extractSubject(jwtToken);

        //Set the authenticated user into the SecurityContext
        //so we get the subject from the token than we extract the username from the subject, find it total details from the securitycontextholder
        //if the user is not authenticated yet (context.getauthentication==null) then define what to do.
        User user = (User)userDetailsService. loadUserByUsername(subject);

        //import the security context holder. It is, stores “who is the user right now?”
        // (username, roles, authentication status) so controllers and services can access it anywhere during the request.
        var context = SecurityContextHolder.getContext();


        // If a valid user is found AND no authentication is set yet,
       // create an authenticated token from the user details
        // and store it in the SecurityContext so Spring knows
       // this request is already authenticated
        if (user != null && context.getAuthentication() == null) {
            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            user,                // authenticated user (principal)
                            null,                // no credentials needed (JWT already validated)
                            user.getAuthorities() // roles/permissions
                    );

            // Attach request details (IP, session info) to the authentication
            authenticationToken.setDetails(request);

            // Save authentication so controllers & security checks can access it
            context.setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);



    }
}
