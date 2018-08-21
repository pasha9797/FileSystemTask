package com.practice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SecurityService securityService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(securityService.TOKEN_HEADER);
        if (authToken != null) {
            if (authToken.contains(" "))
                authToken = authToken.substring(authToken.indexOf(' ') + 1);
            try {
                securityService.SignInByToken(authToken);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    @Autowired
    @Qualifier("authenticationManagerForRest")
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

}