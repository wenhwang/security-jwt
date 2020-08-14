package com.wangwh.code.security.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: security-jwt
 * @description:
 * @author: 40446
 * @create: 2020-05-06 08:46
 **/
@RestController
@Slf4j
@RequestMapping("foo")
public class FooController {

	@GetMapping("ping")
	public String ping(){
		log.debug("FooController ping method invoke");
		return "pong";
	}


	@GetMapping("res")
	@PreAuthorize("hasAuthority('7-RWX')")
	public String accessprotectedResource(){
		log.debug("FooController ping method invoke");
		return "pong";
	}
}
