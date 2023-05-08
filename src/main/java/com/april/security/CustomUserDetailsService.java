package com.april.security;

import com.april.user.UserModel;
import com.april.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = userService.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("該帳號不存在");
		}
		return new CustomUserDetails(user);
	}
}
