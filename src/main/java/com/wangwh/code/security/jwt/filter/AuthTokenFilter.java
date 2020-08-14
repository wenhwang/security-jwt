package com.wangwh.code.security.jwt.filter;

import com.google.gson.Gson;
import com.wangwh.code.security.jwt.utils.JwtTokenUtil;
import com.wangwh.code.security.jwt.utils.R;
import com.wangwh.code.security.jwt.utils.RCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

	public static final String BEARER_PREFIX = "bearer ";

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		if(request.getRequestURI().equals("/auth/token")){
			filterChain.doFilter(request, response);
			return ;
		}

		//filter all request check header has Authorization
		// exists :
		// 			1、vaildate token
		//          2、set check security context current login user
		//not exisit: response must authorization

		String requestToken = request.getHeader("Authorization");
		String jwtToken = "";
		if(StringUtils.hasText(requestToken) && requestToken.startsWith(BEARER_PREFIX)){
			log.debug("The Current Request Token:{}",requestToken);
			try {
				jwtToken = requestToken.substring(7);
				String username = JwtTokenUtil.getUsernameFromToken(jwtToken);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				log.debug("Get user name from token:{} and ", username);
				if(!JwtTokenUtil.validateToken(jwtToken, userDetails)){
					log.debug("token is vaild but user is invalid");
					this.writerResponse(response,
							HttpServletResponse.SC_FORBIDDEN,
							R.fail(RCode.USER_ACCOUNT_DISABLE));
					return ;
				}
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
				filterChain.doFilter(request, response);
			}catch (  SignatureException
						| IllegalArgumentException
						| MalformedJwtException
						| UnsupportedJwtException e) {
				log.error("无效的令牌 : {},{}" ,jwtToken,e.getMessage());
				this.writerResponse(response,
									HttpServletResponse.SC_FORBIDDEN,
									R.fail(RCode.TOKEN_INVAILD));
				return ;
			} catch (ExpiredJwtException e) {
				log.error("令牌失效");
				this.writerResponse(response,
						HttpServletResponse.SC_FORBIDDEN,
						R.fail(RCode.TOKEN_EXPIRED));
				return ;
			}
		}else{
			log.debug("The Current Request not contains Token");
			this.writerResponse(response,
								HttpServletResponse.SC_FORBIDDEN,
								R.fail(RCode.USER_NOT_LOGIN));
		}
	}

	private void writerResponse(HttpServletResponse response,int sc,R r) throws IOException {
		response.setStatus(sc);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(new Gson().toJson(r));
		writer.flush();
		writer.close();
	}


}
