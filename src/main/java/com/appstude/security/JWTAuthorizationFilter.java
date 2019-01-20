package com.appstude.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		           "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,Authorization");
		
		response.addHeader("Access-Control-Expose-Headers",
				            "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization, xsrf-token, Barer, Token");

		//if OPTIONS request no need to look for JWT...
	 	if ("OPTIONS".equals(request.getMethod())) {

			response.setStatus(HttpServletResponse.SC_OK);

			//if  request /login or /register no need to look for JWT...
		}else if("/login".equals(request.getRequestURI()) || "/register".equals(request.getRequestURI())){

			filterChain.doFilter(request, response);
			return;

		}else{

	 		//start looking for JWT..

			String jwtToken = request.getHeader(SecurityConstantes.HEADER_STRING);

			System.out.println("****** jwtToken FROM USER ***** "+jwtToken);

			if(jwtToken == null || !jwtToken.startsWith(SecurityConstantes.TOKEN_PREFIX)){
				filterChain.doFilter(request, response);
				return;
			}

			//decode and sign JWT then  get username and authorities(roles) set them to spring context
			Claims claims = Jwts.parser().setSigningKey(SecurityConstantes.SECRET)
					.parseClaimsJws(jwtToken.replace(SecurityConstantes.TOKEN_PREFIX, ""))
					.getBody();

			//get username from JWT
			String username = claims.getSubject();

			//get authorities(roles) from JWT
			List<String> userRoles = (List<String>) claims.get("roles");

			//ArrayList<Map<String,String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");

			Collection<GrantedAuthority> authorities = new ArrayList<>();

			userRoles.forEach(role->{
				authorities.add(new SimpleGrantedAuthority(role));
			});


			//create authenticatedUser with username and authorities
			UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username,null,authorities);

			//set authenticatedUser to springsecurity context
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

			//pass to the next request
			filterChain.doFilter(request, response);

		}

		
	}

}
