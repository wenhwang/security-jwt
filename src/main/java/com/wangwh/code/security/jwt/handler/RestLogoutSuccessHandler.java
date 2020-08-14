package com.wangwh.code.security.jwt.handler;

import com.google.gson.Gson;
import com.wangwh.code.security.jwt.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		log.debug("用户登出成功");
		R r = R.success();
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = httpServletResponse.getWriter();
		writer.write(new Gson().toJson(r));
		writer.flush();
		writer.close();
	}
}
