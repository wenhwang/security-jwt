package com.wangwh.code.security.jwt.handler;

import com.google.gson.Gson;
import com.wangwh.code.security.jwt.utils.R;
import com.wangwh.code.security.jwt.utils.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest,
					   HttpServletResponse httpServletResponse,
					   AccessDeniedException e) throws IOException, ServletException {
		log.debug("没有权限访问资源");
		R r = R.fail(RCode.NO_PERMISSION);
		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = httpServletResponse.getWriter();
		writer.write(new Gson().toJson(r));
		writer.flush();
		writer.close();
	}
}
