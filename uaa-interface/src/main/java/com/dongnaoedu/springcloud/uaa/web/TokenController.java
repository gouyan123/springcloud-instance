package com.dongnaoedu.springcloud.uaa.web;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.springcloud.uaa.db.UserDomain;
import com.dongnaoedu.springcloud.uaa.db.UserRepository;
import com.dongnaoedu.springcloud.uaa.jwt.JwtConfiguration;
import com.dongnaoedu.springcloud.uaa.jwt.JwtTokenProvider;
import com.dongnaoedu.springcloud.uaa.jwt.UAAClaims;

import io.jsonwebtoken.Claims;

/**
 * 获取token
 * 
 */
@RestController
@RequestMapping("/")
public class TokenController {
	@Autowired
	JwtConfiguration jwtConfiguration;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	UserRepository userRepository;

	// 获取一个根据手机号和密码获取token
	@PostMapping("/token/byPhone")
	public ResponseEntity<?> getTokenByPhone(@RequestBody UserDomain userDomain) {
		// 根据用户名和密码查找用户
		UserDomain domain = userRepository.findByPhoneAndPassword(userDomain.getPhone(), userDomain.getPassword());
		if (domain == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证不通过");
		}
		return ResponseEntity.ok(new JWTToken(jwtTokenProvider.createToken(parseClaims(domain))));
	}

	// 将token反解出来，看看里面的内容；
	// 仅用于开发场景
	@RequestMapping("/token/parse")
	public Claims parseToken(String token) {
		return jwtTokenProvider.parseToken(token);
	}

	// UAAClaims这个对象就是token中的内容
	private UAAClaims parseClaims(UserDomain userDomain) {
		UAAClaims uaaClaims = new UAAClaims();
		uaaClaims.setIssuer(jwtConfiguration.getIss());
		uaaClaims.setIssuedAt(new Date());
		uaaClaims.setAudience(String.valueOf(userDomain.getUserId()));
		uaaClaims.setId(UUID.randomUUID().toString());
		uaaClaims.setUserName(userDomain.getUserName());
		uaaClaims.setExpiration(new Date(System.currentTimeMillis() + jwtConfiguration.getExpm() * 1000 * 60));
		uaaClaims.setEmail(userDomain.getEmail());
		uaaClaims.setPhone(userDomain.getPhone());
		uaaClaims.setSubject(String.valueOf(userDomain.getUserId()));
		uaaClaims.setNotBefore(new Date());
		return uaaClaims;

	}
}
