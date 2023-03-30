package com.att.infrastructure.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceFilter extends HttpFilter {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFilter.class);

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
 
        if (request.getMethod().equals("OPTIONS")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else { 
            filterChain.doFilter(request, response); 
        } 
    }
}