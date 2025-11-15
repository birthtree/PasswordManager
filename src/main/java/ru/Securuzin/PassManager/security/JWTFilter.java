package ru.Securuzin.PassManager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter{

    private final JwtProvider jwtProvider;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Вытаскиваем токен из Authorization header
            String token = getTokenFromRequest(request);

            // Если токен есть и валиден
            if (token != null && jwtProvider.isTokenValid(token)) {
                String username = jwtProvider.getUsername(token);
                Long userId = jwtProvider.getUserId(token);

                // Создаём Authentication объект и устанавливаем его в контекст
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                auth.setDetails(userId);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("JWT filter error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Вытаскивает JWT токен из Authorization header
     * Формат: "Bearer <token>"
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Удаляем "Bearer "
        }

        return null;
    }
}
