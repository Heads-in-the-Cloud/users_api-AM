package com.ss.training.utopia.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends BasicAuthenticationFilter {

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Endpoint hit by every request
     * @param request Http request with header information
     * @param response response template to begin building response
     * @param chain the filter chain we are using
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // grab header
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // check header exists and is correctly formatted
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // get verified
        Authentication authentication = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // continue filter
        chain.doFilter(request, response);
    }

    /**
     * Parses the Http request to determine user authorization
     * @param request Http request to parse Jwt Tokens from
     * @return the authorizations (if found), or null otherwise
     */
    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {

        // grab token
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            // get JWT token info
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET))
                .build()
                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

            // check real user
            String username = jwt.getSubject();
            if (username == null) return null;

            // get authorities
            String claims = jwt.getClaim(SecurityConstants.AUTHORITY_CLAIM)
                .asString();
            List<SimpleGrantedAuthority> authorities =
                new ArrayList<>(Arrays.asList(claims.split(",")))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // get uid
            Integer userId = jwt.getClaim(SecurityConstants.USER_ID_CLAIM).asInt();

            // compose
            return new UsernamePasswordAuthenticationToken(userId, null, authorities);
        }
        return null;
    }
}
