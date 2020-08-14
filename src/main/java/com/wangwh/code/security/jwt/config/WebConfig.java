package com.wangwh.code.security.jwt.config;

import com.wangwh.code.security.jwt.filter.AuthTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class WebConfig {

	@Bean
	public FilterRegistrationBean<AuthTokenFilter> registrationBean(){
		FilterRegistrationBean<AuthTokenFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new AuthTokenFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("Auth Token Filter");
		registrationBean.setOrder(1);
		return registrationBean;
	}
}
