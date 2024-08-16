package com.hyong.jwt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class MyFilter1 implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String headerAuth = (String) req.getHeader("Authorization");
		
		// 토근 생성
		if("cos".equals(headerAuth)) {
			System.out.println("토큰 : " + headerAuth);
			chain.doFilter(req, res);
		}else {
			res.getWriter().println("인증안됨");
		}
		
		/**
		 * 요청 시 마다 filter 작동 됨 : 필터2(security filter before) > 필터3(security filter after) > 토큰 : cos(doFilter)
		 * javascript post 요청 방법
		 * 
		 * 1. var xhr = new XMLHttpRequest;
		 * 2. xhr.open('POST', 'http://localhost:8080/token', true);
		 * 3. xhr.setRequestHeader('Content-Type', 'application/json');
		 * 4. xhr.setRequestHeader('Authorization', 'cos');
		 * 5. var data = JSON.stringify({ key1: 'value1', key2: 'value2' }); //json 데이터 세팅
		 * 6. xhr.send(data);
		 * 
		 */
	}

}
