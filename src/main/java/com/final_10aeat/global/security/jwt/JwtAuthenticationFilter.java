package com.final_10aeat.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.global.exception.ErrorCode;
import com.final_10aeat.global.security.principal.AdminDetailsProvider;
import com.final_10aeat.global.security.principal.ManagerDetailsProvider;
import com.final_10aeat.global.security.principal.MemberDetailsProvider;
import com.final_10aeat.global.util.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Controller에서 로그인 정보 사용시 아래와 같은 방식으로 사용 다운 캐스팅 필수! ManagerPrincipal principal =
     * (ManagerPrincipal) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
     */

    private final JwtTokenGenerator jwtTokenGenerator;
    private final MemberDetailsProvider memberDetailsProvider;
    private final ManagerDetailsProvider managerDetailsProvider;
    private final AdminDetailsProvider adminDetailsProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        //헤더에서 토큰 값을 읽어오는 과정
        String accessToken = request.getHeader("accessToken");
        try {
            if (accessToken != null) {
                Authentication authentication = getEmailPassword(accessToken);

                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            }


            filterChain.doFilter(request, response);//필터 종료 후 다음 필터로 진행
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response);
        }
    }

    private void jwtExceptionHandler(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
            new ObjectMapper().writeValueAsString(ResponseDTO.error(ErrorCode.EXPIRED_TOKEN)));
    }


    private Authentication getEmailPassword(String token) {
        String email = jwtTokenGenerator.getUserEmail(token);
        MemberRole role = jwtTokenGenerator.getRole(token);
        if (email != null && (role != MemberRole.TENANT)) {
            UserDetails userDetails = getUserDetails(email, role);
            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        }
        return null;
    }

    private UserDetails getUserDetails(String email, MemberRole role) {
        if (role == MemberRole.ADMIN) {
            return adminDetailsProvider.loadUserByUsername(email);
        }
        if (role == MemberRole.MANAGER) {
            return managerDetailsProvider.loadUserByUsername(email);
        }
        return memberDetailsProvider.loadUserByUsername(email);
    }
}
