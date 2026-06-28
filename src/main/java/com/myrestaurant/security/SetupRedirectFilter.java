package com.myrestaurant.security;

import com.myrestaurant.service.ConfigService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Tant que la configuration initiale n'a pas ete realisee, redirige toute
 * requete vers la page de configuration /setup (hors assets et endpoints techniques).
 */
@Component
@Order(1)
public class SetupRedirectFilter extends OncePerRequestFilter {

    private final ConfigService configService;

    public SetupRedirectFilter(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (isExcluded(path) || configService.isConfigured()) {
            filterChain.doFilter(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/setup");
    }

    private boolean isExcluded(String path) {
        return path.startsWith("/setup")
                || path.startsWith("/placeholder")
                || path.startsWith("/uploads")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")
                || path.startsWith("/webjars")
                || path.startsWith("/error")
                || path.startsWith("/favicon");
    }
}
