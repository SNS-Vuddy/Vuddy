package com.buddy.jwt;

import com.buddy.model.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long tokenValidityInMilliseconds;
   private final long refreshTokenValidityInMilliseconds;
   private static Key key;

   private final UserRepository userRepository;

//   @Autowired
//   private RedisTemplate<String, String> redisTemplate;

   public TokenProvider(
           @Value("${jwt.secret}") String secret,
           @Value("${jwt.token-validity-in-seconds}") Long tokenValidityInSeconds,
           @Value("${jwt.refresh-token-validity-in-seconds}") Long refreshTokenValidityInMilliseconds, UserRepository userRepository) {
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
      this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
      this.userRepository = userRepository;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   public String createAccessToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);

      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim(AUTHORITIES_KEY, authorities)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }

   public String createRefreshToken(Authentication authentication, Long userId) {
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

      return Jwts.builder()
              .setSubject(authentication.getName())
              .claim(AUTHORITIES_KEY, authorities)
              .claim("userId", userId)
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
   }

   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         logger.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         logger.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }

   public static String validateTokenErrorMsg(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return null;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         return "잘못된 JWT 서명입니다.";
      } catch (ExpiredJwtException e) {
         return "만료된 JWT 토큰입니다.";
      } catch (UnsupportedJwtException e) {
         return "지원되지 않는 JWT 토큰입니다.";
      } catch (IllegalArgumentException e) {
         return "JWT 토큰이 잘못되었습니다.";
      }
   }

   // 유저 아이디 레디스로 관리
//   public Long getUserIdFromToken(String token) {
//
//      String accessToken = token.substring(7);
//
//      ValueOperations<String, String> vop = redisTemplate.opsForValue();
//
//
//      String redisUserId = vop.get(accessToken);
//      if (redisUserId == null) {
//         System.out.println("=================================================================");
//         System.out.println("레디스에 정보가 없다.");
//         System.out.println("=================================================================");
//
//         String userNickname = Jwts
//                 .parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(accessToken)
//                 .getBody()
//                 .getSubject();
//
//         Long userId = userRepository.findByNickname(userNickname).getId();
//            vop.set(accessToken, userId.toString());
//            return userId;
//      } else {
//         System.out.println("=================================================================");
//         System.out.println("레디스에서 가져왔따.");
//         System.out.println("=================================================================");
//
//         return Long.parseLong(redisUserId);
//      }
//
//   }

   public String getUserNicknameFromToken(String token) {

      String assertToken = token.substring(7);

      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(assertToken)
              .getBody();

      return claims.getSubject();
   }

   public Long getUserIdFromRefreshToken(String token) {
      Claims claims = Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      return claims.get("userId", Long.class);
   }

}
