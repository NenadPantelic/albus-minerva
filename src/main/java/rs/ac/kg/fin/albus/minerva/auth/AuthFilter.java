package rs.ac.kg.fin.albus.minerva.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.kg.fin.albus.minerva.exception.ApiException;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-albus-user-id";
    private static final String USER_ROLE_HEADER = "X-albus-user-role";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = getAuthentication(request);
            log.info("Authenticating user {}....", authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            log.warn("Could not authenticate the user.", e);
            setErrorResponse(
                    response,
                    e.getMessage(),
                    e.getStatusCode()
            );
        } catch (
                RuntimeException e) {
            log.error("Could not authenticate the user.", e);
            setErrorResponse(
                    response,
                    "Internal server error.",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }

    }


    private Authentication getAuthentication(HttpServletRequest request) {
        final String userId = request.getHeader(USER_ID_HEADER);
        final String userRole = request.getHeader(USER_ROLE_HEADER);

        // TODO throw unauthorized if necessary, roles
        if (userId == null || userId.isBlank()) {
            //
        }

        if (userRole == null || userRole.isBlank()) {
            //
        }

        return new UsernamePasswordAuthenticationToken(
                new UserContext(userId, Role.valueOf(userRole)),
                null,
                List.of()
        );
    }

    private void setErrorResponse(HttpServletResponse response,
                                  String message,
                                  int statusCode) {

    }

}
