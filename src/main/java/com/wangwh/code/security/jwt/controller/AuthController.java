package com.wangwh.code.security.jwt.controller;

import com.wangwh.code.security.jwt.model.UserAuthForm;
import com.wangwh.code.security.jwt.utils.JwtTokenUtil;
import com.wangwh.code.security.jwt.utils.R;
import com.wangwh.code.security.jwt.utils.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

    @PostMapping("/token")
	public R<?>  authentication(@RequestBody UserAuthForm userAuthForm){
		String username = userAuthForm.getUsername();
		String password = userAuthForm.getPassword();
		//UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		try {
			//authentication user info
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			UserDetails principal = (UserDetails) authenticate.getPrincipal();

			//gen user token
			String token = JwtTokenUtil.generateToken(principal);
			log.debug("gen token  :{} ",token);
			Map<String,String> tokenMap = new HashMap<>();
			tokenMap.put("token",token);
			return R.success(tokenMap);
		}catch (DisabledException e){
			log.debug("账号被禁用");
			return R.fail(RCode.USER_ACCOUNT_DISABLE);
		}catch (LockedException e){
			log.debug("账号被锁定");
			return R.fail(RCode.USER_ACCOUNT_LOCKED);
		}catch (BadCredentialsException e){
			log.debug("账号或密码错误");
			return R.fail(RCode.USER_ACCOUNT_NOT_EXIST);
		}catch (AuthenticationException e){
			log.debug("账号或密码认证失败");
			return R.fail(RCode.USER_CREDENTIALS_ERROR);
		}
	}

}
