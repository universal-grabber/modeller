package net.tislib.ugm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import net.tislib.ugm.api.data.AuthResponse;
import net.tislib.ugm.api.data.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

import static net.tislib.ugm.api.SecurityConstants.EXPIRATION_TIME;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/1.0/login", "POST"));
        this.authenticationManager = authenticationManager;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        User applicationUser = objectMapper.readValue(req.getInputStream(), User.class);

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(applicationUser.getUsername(),
                        applicationUser.getPassword(), applicationUser.getAuthorities())
        );
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Claims claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
        byte[] key = Base64.getEncoder().encode(SecurityConstants.KEY.getBytes());

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(exp)
                .compact();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser((UserDetails) auth.getPrincipal());

        objectMapper.writeValue(res.getOutputStream(), authResponse);

    }
}
