package com.wangwh.code.security.jwt.utils;

import com.google.common.base.Joiner;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wangwh.code.security.jwt.utils.JwtTokenUtil.ExpiredTime.MINUTES_15;

/**
 * @program: security-jwt
 * @description:
 * @author: 40446
 * @create: 2020-05-06 13:00
 **/
@Slf4j
public class JwtTokenUtil {

	enum ExpiredTime{
		ONE_DAY (24*60*60*1000),
		HALF_DAY (ONE_DAY.millosecond/2),
		ONE_HOUR (HALF_DAY.millosecond/12),
		MINUTES_30 (ONE_HOUR.millosecond/2),
		MINUTES_15 (MINUTES_30.millosecond/2),
		ONE_MINUTES (MINUTES_15.millosecond/15);
		private final int millosecond;

		ExpiredTime(int millosecond) {
			this.millosecond = millosecond;
		}
	}

	public static final String USERNAME = "username";

	public static final String SCOPES = "scopes";

	//token 失效时间
	private static final long TOKEN_EXPIRED_TIME = MINUTES_15.millosecond;

	//加解密密钥
	private static final String SECRET_KEY = "wangwenhong1601221009yxwang1227wenyao";

	//加解密算法
	private static final SignatureAlgorithm HS256 = SignatureAlgorithm.HS256;



	public static void main(String[] args) {

		UserDetails userDetails = User.withUsername("wangwh")
										.password("123456")
										.roles("admin")
										.authorities("7-rwx","5-rx","3-wx","6-rx")
										.build();
		String token = JwtTokenUtil.generateToken(userDetails);
		log.debug("生成 Token : " + token);

		log.debug("用户名:{}",JwtTokenUtil.getUsernameFromToken(token));
		log.debug("权限:{}",JwtTokenUtil.getScopesFromToken(token));
		log.debug("是否失效:{}",JwtTokenUtil.isTokenExpired(token));
		log.debug("校验Token:{}",JwtTokenUtil.validateToken(token,userDetails));



	}


	private static String doGenerateToken(Map<String, Object> claims, String subject) {
		JwtBuilder jwtBuilder = Jwts.builder()
								.setHeaderParam("typ", "JWT")
								.setId(UUID.randomUUID().toString())
								.setClaims(claims)
								.setSubject(subject)
								.setIssuedAt(new Date(System.currentTimeMillis()))
								.signWith(getSecretKey(),SignatureAlgorithm.HS256);

		if(TOKEN_EXPIRED_TIME > 0){
			jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME));
		}

		return jwtBuilder.compact();
	}

	public static String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(USERNAME, userDetails.getUsername());
		claims.put(SCOPES, Joiner.on(" ").join(userDetails.getAuthorities()
															.stream()
															.map(GrantedAuthority::getAuthority)
															.collect(Collectors.toList())));
		return doGenerateToken(claims, userDetails.getUsername());
	}


	public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}


	private static Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
					.setSigningKey(getSecretKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}


	public static String getSubjectFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public static String getUsernameFromToken(String token) {
		return getClaimFromToken(token, claims -> claims.get(USERNAME,String.class));
	}

	public static String getScopesFromToken(String token) {
		return getClaimFromToken(token, claims -> claims.get(SCOPES, String.class));
	}

	public static Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private static Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}


	//validate token
	public static Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	//base64 密钥
	private static Key getSecretKey(){
		String encode = Encoders.BASE64.encode(SECRET_KEY.getBytes());
		return Keys.hmacShaKeyFor(encode.getBytes());

	}

	//token 失效时间
	private static Date  getExpiredMillis(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentTimeMillis = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
		Date expiredDateTime = new Date(currentTimeMillis+TOKEN_EXPIRED_TIME);
		log.debug("Token 失效时间:" + formatter.format(expiredDateTime));
		return expiredDateTime;
	}

}
