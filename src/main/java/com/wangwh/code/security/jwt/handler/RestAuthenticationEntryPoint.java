package com.wangwh.code.security.jwt.handler;

import com.google.gson.Gson;
import com.wangwh.code.security.jwt.utils.R;
import com.wangwh.code.security.jwt.utils.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest httpServletRequest,
						 HttpServletResponse httpServletResponse,
						 AuthenticationException e) throws IOException, ServletException {
		log.debug("用户未登录");
		R r = R.fail(RCode.USER_NOT_LOGIN);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = httpServletResponse.getWriter();
		writer.write(new Gson().toJson(r));
		writer.flush();
		writer.close();
	}
}
