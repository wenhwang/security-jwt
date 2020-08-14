package com.wangwh.code.security.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.UUID;

/**
 * @program: security-jwt
 * @description:
 * @author: 40446
 * @create: 2020-05-06 09:52
 **/
public class JwtTest {


	@Test
	public void getJWTTest(){
		Key key = Keys.hmacShaKeyFor("d2FuZ3dlbmhvbmcxNjAxMjIxMDA5eXh3YW5nMTIyN3dlbnlhbw==".getBytes());
		String jws = Jwts.builder()
							.setHeaderParam("typ","JWT")
							.setId(UUID.randomUUID().toString())
							.claim("name","wangwh")
							.claim("authors","p1 p2 p3")
							.setSubject("JDKONG")
							.signWith(key,SignatureAlgorithm.HS256)
							.compact();
		System.out.println(jws);


		Jwt parse = Jwts.parserBuilder().setSigningKey(key).build().parse(jws);
		Object body = parse.getBody();
		Header header = parse.getHeader();
		System.out.println(body);
		System.out.println(header);


	}

}
