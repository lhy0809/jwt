package com.hyong.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyong.jwt.config.auth.PrincipalDetails;
import com.hyong.jwt.model.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	// login 요청이 들어오면 동작하는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println(">>>>>>>> attempAuthentication 로그인 시도 중");
		/*
		 * 1. username, password 받아서 
		 * 2. 정상인지 로그인 시도, authenticationManager을 통해서 로그인 시도를 하면 PrincipalDetailsService가 > loadUserByUsername 함수가 호출
		 * 3. PrincipalDetails 를 세션에 담고 (권한 관리를 위해서 > 권한이 없으면 담지 않아도 된다.)
		 * 4. JWT토큰을 만들어서 응답.
		 */
		try {
			// 1번
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
			
			// 2번
			Authentication authentication = getAuthenticationManager().authenticate(token);
			
			// 3번 확인.
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername());
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// attemptAuthentication 함수 실행 후 인증이 정상적으로 되었으면 함수가 실행된다.
	// 4. JWT 토큰 생성해서  request 요청한 사용자에게 JWT토큰 response.
	@Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
		System.out.println("인증이 완료 됨.");
		//super.successfulAuthentication(request, response, chain, authResult);
    }
}

