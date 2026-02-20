package lu.perso.menuback.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserCodeFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger(UserCodeFilter.class);
    private static final String HEADER_NAME = "X-User-Code";

    @Value("${app.security.user-code}")
    private String expectedUserCode;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userCode = request.getHeader(HEADER_NAME);

        if (userCode == null || !userCode.equals(expectedUserCode)) {
            log.warn("Rejected request to {} — invalid or missing {}", request.getRequestURI(), HEADER_NAME);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Unauthorized: invalid or missing X-User-Code header\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip CORS preflight requests
        return HttpMethod.OPTIONS.matches(request.getMethod())
            || request.getRequestURI().endsWith("/status");
    }
}
