package com.miftah.lamaecommerse.configurations.security;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.miftah.lamaecommerse.repositories.user.UserRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private UserRepository userRepository;

	private CustomJwtFilter jwtFilter;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Value("${jwt.public.key}")
	private RSAPublicKey rsaPublicKey;

	@Value("${jwt.private.key}")
	private RSAPrivateKey rsaPrivateKey;

	@Value("${secret}")
	private String secretKey;

	@Autowired
	public SecurityConfig(@Lazy CustomJwtFilter customJwtFilter) {
		this.jwtFilter = customJwtFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
			BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		return (AuthenticationManager) httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(username -> {
					try {
						return userRepository.findByUsername(username);
					} catch (Exception e) {
						throw new UsernameNotFoundException(String.format("User: %s, not found", username));
					}
				}).passwordEncoder(bCryptPasswordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().formLogin().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling(exp -> exp.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
				.accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
		http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/product/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").permitAll().antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated();

		http.httpBasic(Customizer.withDefaults()).oauth2ResourceServer().jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());
		http.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		var jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException {
//		SecretKey secretKey = new SecretKeySpec(this.secretKey.getBytes(), "HMACSHA512");
//		SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
//		String encKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

//		System.out.println("encKey " + encKey);

//		byte[] decodedkey = Base64.getDecoder().decode(this.secretKey);
//		SecretKey originalKey = new SecretKeySpec(decodedkey, "HMACSHA512");

//		return NimbusJwtDecoder.withSecretKey(originalKey).build();
		return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
