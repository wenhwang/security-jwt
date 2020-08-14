package com.wangwh.code.security.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
public class RestSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

		@Override
		public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
			log.debug("--------> 回话失效");
		}
	}