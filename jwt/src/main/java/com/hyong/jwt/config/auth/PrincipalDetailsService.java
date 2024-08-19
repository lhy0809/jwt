package com.hyong.jwt.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hyong.jwt.model.User;
import com.hyong.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login 시에 UserDetailsService 타입으로 IoC 되어있는 클래스의 loadUserByUsername 이 동작한다.
// 단, Security Config 에 formLogin() 을 disabled 했기 때문에 동작하지 않음.
// > 수동으로 실행
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		return new PrincipalDetails(userEntity);
	}

}
