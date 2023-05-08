package com.april.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/*~~(Migrate manually based on https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)~~>*/
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
//		以下寫死的為測試用
//		auth.inMemoryAuthentication()
//				.withUser("admin")
//				.password(passwordEncoder().encode("123"))
//				.roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http
				.authorizeRequests()
					.antMatchers("/register", "/login").permitAll()
					.anyRequest().authenticated()   // 正式使用時要求瀏覽需要依照權限顯示
//					.anyRequest().permitAll()   // 測試時暫時開放權限
					.and()
				.formLogin()
					.loginPage("/login")
					.failureUrl("/login?error")
					.and()
				.logout()
				.logoutSuccessUrl("/login").permitAll();
	}

//你好, 到了spring security 6.0 那兩個configure 已經變成這樣了.
//	@Bean
//	public InMemoryUserDetailsManager userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("admin")
//				.password(passwordEncoder().encode("123"))
//				.roles("ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(user);
//	}
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		/* anymatchers() deprecated and removed from spring security 6.0, use requestMatchers instead */
//		http.cors().and().csrf().disable()
//				.authorizeRequests().requestMatchers("/register").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin();
//
//		// http....;
//
//		return http.build();
//	}
	
}