package com.edu.ssafy.auth.jwt;

import com.edu.ssafy.auth.model.dto.response.ErrorRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

      ErrorRes errorResponse = new ErrorRes(403, "권한이 없습니다.");

      response.setContentType("application/json;charset=UTF-8");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);

      ObjectMapper objectMapper = new ObjectMapper();
      String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
      response.getWriter().write(errorResponseJson);
   }
}