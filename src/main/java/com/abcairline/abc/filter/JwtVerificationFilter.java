package com.abcairline.abc.filter;

import com.abcairline.abc.domain.User;
import com.abcairline.abc.exception.InvalidJwtTokenException;
import com.abcairline.abc.repository.UserRepository;
import com.abcairline.abc.service.auth.JwtTokenVerifier;
import com.abcairline.abc.service.auth.constant.JwtConstants;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class JwtVerificationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    private JwtTokenVerifier jwtTokenVerifier;

    public JwtVerificationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(JwtConstants.HEADER_AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            String accessToken = authHeader.replace(JwtConstants.TOKEN_PREFIX, "");

            try {
                jwtTokenVerifier = new JwtTokenVerifier(JwtConstants.SECRET_KEY);
                DecodedJWT decodedJWT = jwtTokenVerifier.verify(accessToken);
                Long id = Long.parseLong(decodedJWT.getSubject());

                Authentication authentication = null;

                if (id != null) {
                    User user = userRepository.findOne(id);
                    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                    if (user != null)
                        authorities.add(() -> user.getRole());
                        authentication = new UsernamePasswordAuthenticationToken(id, null, authorities);
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (InvalidJwtTokenException ex) {
                log.error(ex.getMessage());
                setUnauthorizedResponse(response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void setUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // HTTP 응답 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String message = "{\"message\": \"Access Token is invalid. Unauthorized\"}";
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
