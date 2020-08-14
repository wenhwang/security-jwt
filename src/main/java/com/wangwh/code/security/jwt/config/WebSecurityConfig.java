package com.wangwh.code.security.jwt.config;

import com.wangwh.code.security.jwt.filter.AuthTokenFilter;
import com.wangwh.code.security.jwt.handler.RestAccessDeniedHandler;
import com.wangwh.code.security.jwt.handler.RestAuthenticationEntryPoint;
import com.wangwh.code.security.jwt.handler.RestLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return NoOpPasswordEncoder.getInstance();
	}

    @Bean
	public UserDetailsService userDetailsService(){
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(User.withUsername("wangwh")
											.password("123")
											.roles("admin")
											.authorities("7-RWX-1").build());
		return userDetailsManager;
	}

	@Bean
	public RestLogoutSuccessHandler logoutSuccessHandler(){
		return new RestLogoutSuccessHandler();
	}

	@Bean
	public RestAuthenticationEntryPoint authenticationEntryPoint(){
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public RestAccessDeniedHandler accessDeniedHandler(){
		return new RestAccessDeniedHandler();
	}

	@Bean
	public AuthTokenFilter authTokenFilter(){
		return new AuthTokenFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
					.antMatchers("/auth/token").permitAll()
					.anyRequest().authenticated()
				.and()
					.logout()
					.logoutUrl("/auth/logout")
					.logoutSuccessHandler(logoutSuccessHandler())
				.and()
					.exceptionHandling()
					.accessDeniedHandler(accessDeniedHandler())
					.authenticationEntryPoint(authenticationEntryPoint())
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
					.headers()
					.cacheControl();
	}





}
