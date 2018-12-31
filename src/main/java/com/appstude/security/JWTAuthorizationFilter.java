package com.appstude.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.appstude.utilis.SecurityConstantes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		response.addHeader("Access-Control-Allow-Headers",
		           "Origin, Accept, X-Requested-With, Content-Type, "
	             + "Access-Control-Request-Method, "
	             + "Access-Control-Request-Headers,Authorization");
		
		response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization, xsrf-token, Barer, Token");
		
	  /*	if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		}*/
	
	
		
		String jwtToken = request.getHeader(SecurityConstantes.HEADER_STRING);
		
		System.out.println("****** jwtToken FROM USER ***** "+jwtToken);
		
         if(jwtToken == null || !jwtToken.startsWith(SecurityConstantes.TOKEN_PREFIX)){
			filterChain.doFilter(request, response); 
			return;
		}
		
	
			Claims claims = Jwts.parser().setSigningKey(SecurityConstantes.SECRET)
				       .parseClaimsJws(jwtToken.replace(SecurityConstantes.TOKEN_PREFIX, ""))
				       .getBody();
		
		String username = claims.getSubject();
		
		ArrayList<Map<String,String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		roles.forEach(r->{	
			authorities.add(new SimpleGrantedAuthority(r.get("authority")));
		});
		
		
		UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username, null,authorities);
		
		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		
		filterChain.doFilter(request, response); 

		
		
		
	}

}
