package com.hyong.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import com.hyong.jwt.filter.MyFilter2;
import com.hyong.jwt.filter.MyFilter3;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsFilter corsFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// http.addFilter(new MyFilter1());
		// 오류 발생 > MyFilter1은 타입이 filter이기 때문에 SecurityFilter에 주입이 안 된다.
		// addFilterBefore 또는 addFilterAfter을 통해 SecurityFilter의 어떤 filter를 실행하기 전/후에 실행해라.
		http.addFilterBefore(new MyFilter2(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);
		// 순서 : security 필터 > 일반 필터 
		// first security filter > ... > MyFilter2 > ... > BasicAuthenticationFilter > Myfilter3 > ... > last security filter > MyFilter1(일반 필터)
		
		http.csrf(AbstractHttpConfigurer::disable);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless 상태로 만들겠다 > session을 사용하지 않겠다.
		.and()
		.addFilter(corsFilter) // 인증이 있는 경우는 filter에 등록을 해줘야 한다. > @CrossOrigin(인증이 없을 경우)
		.formLogin().disable()
		.httpBasic().disable()
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(new AntPathRequestMatcher("/api/v1/user/**")).hasAnyRole("USER","MANAGER","ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/api/v1/manager/**")).hasAnyRole("MANAGER","ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/api/v1/admin/**")).hasRole("ADMIN")
				.anyRequest().permitAll()
		);
		return http.build();
	}

}
