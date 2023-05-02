package com.buddy.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SecurityUtil {

   private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

   private SecurityUtil() {}

   public static Optional<String> getCurrentUsername() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         logger.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(username);
   }

   public static Map<String, String> getCurrentUser() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || authentication.getName() == null) {
         throw new RuntimeException("No authentication information.");
      }
      try {
         log.info("여기에 id가 들어갈까");
         log.info(((User) authentication.getPrincipal()).getPassword());
         Map<String, String> authInfo = new HashMap<>();
         authInfo.put("uId", ((User) authentication.getPrincipal()).getPassword());
         authInfo.put("email", authentication.getName());
         authInfo.put("role", authentication.getAuthorities().toString());
         return authInfo;
      } catch (Exception e){
         throw new RuntimeException("No authentication information.");
      }
   }
}
