package com.swcamp9th.springsecuritypratice.security;

import com.swcamp9th.springsecuritypratice.member.command.application.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private MemberService memberService;

    public JwtUtil(
        @Value("${token.secret}") String secretKey,
        MemberService memberService)
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberService = memberService;
    }

    /* 설명 .Token 검증
     *       - Bearer 토큰이 넘어왔는지
     *       - 우리 사이트의 secret Key로 만들어 졌는지
     *       - 만료되었는지
     *       - 내용이 비어있지 않은지
     * */
    public boolean validateToken(String token) {

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT token {}", e);
        } catch (ExpiredJwtException e){
            log.info("Expired JWT token {}", e);
        } catch (UnsupportedJwtException e){
            log.info("Unsirppoted JWT token {}", e);
        } catch (IllegalArgumentException e){
            log.info("JWT claims string is empty {}", e);
        }

        return true;
    }
    
    /* 설명. 넘어 온 AcessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token) {

        /* 설명. 로그인 시, 토큰을 들고 왔던 들고 오지 않았던 동일하게, security가 관리할 UserDetails 타입을 정의 */
        UserDetails userDetails = memberService.loadUserByUsername(this.getUserId(token));

        /* 설명. token에서 claim 추출 */
        Claims claims = parseClaims(token);
        log.info("넘어온 AccessToken claims 확인 : {}", claims);
        Collection<? extends GrantedAuthority> authorities;

        if(claims.get("auth") == null)
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        else{

            /* 설명. claim에서 권한 정보 가져오기 */
           authorities = Arrays.stream(claims.get("auth").toString()
                   .replace("[", "").replace("]","").split(", "))
                   .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        }


        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /* 설명. Token에서 사용자의 id(subject claim) 추출*/
    private String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    /* 설명. Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}