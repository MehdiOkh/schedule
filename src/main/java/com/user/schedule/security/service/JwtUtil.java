package com.user.schedule.security.service;

import com.user.schedule.database.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final String SECRET_KEY = "secret";
    public String extractUsername(String token){
        return  extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return  extractClaim(token, Claims::getExpiration);
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        List<SimpleGrantedAuthority> roles = null;
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isMaster = claims.get("isMaster", Boolean.class);
        Boolean isStudent = claims.get("isStudent", Boolean.class);
        if (isAdmin != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (isMaster != null && isMaster) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_MASTER"));
        }
        if (isStudent != null && isStudent) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }
        return roles;
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_MASTER"))) {
            claims.put("isMaster", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            claims.put("isStudent", true);
        }
        return createToken(claims, userDetails.getUsername());
    }
    private String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60* 24*7))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
