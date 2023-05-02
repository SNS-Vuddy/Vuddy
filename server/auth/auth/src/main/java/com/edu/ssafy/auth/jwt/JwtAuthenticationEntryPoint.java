package com.edu.ssafy.auth.jwt;

import com.edu.ssafy.auth.model.dto.response.ErrorRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {

      // 액세스 토큰 정보
      String accessToken = request.getHeader("Authorization").substring(7);

      // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
      String msg = TokenProvider.validateTokenErrorMsg(accessToken);
      ErrorRes errorResponse = new ErrorRes(401, msg);

      // 응답 컨텐츠와 상태값을 셋팅해준다.
      response.setContentType("application/json;charset=UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // JSON 형식으로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
      response.getWriter().write(errorResponseJson);
   }
}