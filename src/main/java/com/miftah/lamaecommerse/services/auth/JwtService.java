package com.miftah.lamaecommerse.services.auth;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.utils.ObjectMapper;

@Service
public class JwtService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Value("${secret}")
	private String jwtSecret;

	@Autowired
	private JwtEncoder jwtEncoder;

	public UserView generateToken(String username, String password) throws Exception {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			var userLogin = (User) authentication.getPrincipal();
			var expired = 600L;
			String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));

//			Map<String, Object> claims = new HashMap<>();
//
//			claims.put("username", userLogin.getUsername());
//			claims.put("user_id", userLogin.getId());
//			claims.put("role", authorities);
//
//			var token = Jwts.builder().setClaims(claims).setSubject(userLogin.getUsername())
//					.setIssuedAt(new Date(System.currentTimeMillis()))
//					.setExpiration(new Date(System.currentTimeMillis() + expired * 1000))
//					.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

			var claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(Instant.now())
					.expiresAt(Instant.now().plusSeconds(expired)).subject(String.format(userLogin.getUsername()))
					.claim("role", authorities).build();

			var token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
			return ObjectMapper.userToUserView(userLogin, token);
		} catch (BadCredentialsException e) {
			throw e;
		}

	}

}
