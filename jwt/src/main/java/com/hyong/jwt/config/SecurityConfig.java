package com.hyong.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import com.hyong.jwt.config.auth.PrincipalDetailsService;
import com.hyong.jwt.config.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsFilter corsFilter;
	
	private final PrincipalDetailsService principalDtailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectPostProcessor<Object> objectPostProcessor;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless 상태로 만들겠다 > session을 사용하지 않겠다.
		.and()
		.addFilter(corsFilter) // 인증이 있는 경우는 filter에 등록을 해줘야 한다. > @CrossOrigin(인증이 없을 경우)
		.addFilter(getAuthenticationFilter())
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
	
	 public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(principalDtailsService).passwordEncoder(bCryptPasswordEncoder);
	        return auth.build();
	    }

	    private JwtAuthenticationFilter getAuthenticationFilter() throws Exception {
	    	JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
	        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
	        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
	        return authenticationFilter;
	    }


}
