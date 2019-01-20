package com.appstude.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appstude.entities.AppUser;
import com.appstude.utilis.SecurityConstantes;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		AppUser appUser = null;

		try {
			//convert data form JSON to java Object
			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			System.out.println("***** USER AUTHENTICATED *****");
			System.out.println("username : "+appUser.getUsername()+" , password :"+appUser.getPassword());

			// return authenticated user to spring
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		//authentication successful , use authenticatd user info to generate JWT
		User springUser = (User) authResult.getPrincipal();

		List<String> userRoles = new ArrayList<String>();

		springUser.getAuthorities().forEach(a->{
			   userRoles.add(a.getAuthority());
		});

		String jwtToken=Jwts.builder()
				.setIssuer(request.getRequestURI())
		        .setSubject(springUser.getUsername())
		        .setExpiration(new Date(System.currentTimeMillis()+SecurityConstantes.EXPIRATION_TIME))
		        .signWith(SignatureAlgorithm.HS256, SecurityConstantes.SECRET)
		        //.claim("roles", springUser.getAuthorities().toArray())
				.claim("roles", userRoles)
		        .compact();
		
		response.addHeader(SecurityConstantes.HEADER_STRING, jwtToken);
		
	}
}
