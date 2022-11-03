package com.miftah.lamaecommerse.configurations.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.utils.HttpStatusConverter;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {
//	@Autowired
//	private UserService userService;

	@Autowired
	private JwtDecoder jwtDecoder;

//	@Autowired
//	private HandlerExceptionResolver handlerExceptionResolver;

	@Value("${secret}")
	private String secret;

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		String tokenHeader = request.getHeader("Authorization");
//
//		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
//			try {
//				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder
//						.getContext().getAuthentication();
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//				filterChain.doFilter(request, response);
//			} catch (AccessDeniedException e) {
//				handlerExceptionResolver.resolveException(request, response, null, e);
//			}
//		}
//
//	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tokenHeader = request.getHeader("Authorization");
//		String username = null;
		String token = null;

		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			token = tokenHeader.substring(7);

			try {
				System.out.println("CustomJwtFilter.doFilterInternal");

//				Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
				this.jwtDecoder.decode(token);
//				username = (String) claims.get("username");
//
//				UserDetails userDetails = userService.loadUserByUsername(username);
//				String checkUserDb = userDetails.getUsername();
//
//				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//					if (!checkUserDb.equals(username))
//						throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED, "Not authorized",
//								"jwt token filter");
//					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//							userDetails, null, userDetails.getAuthorities());
//					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//				}

			} catch (BadJwtException e) {
				sendError(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
			} catch (JwtEncodingException e) {
				sendError(response, e.getMessage(), HttpServletResponse.SC_NOT_ACCEPTABLE);
			} catch (AccessDeniedException e) {
				sendError(response, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
			}
		}

		filterChain.doFilter(request, response);
	}

	private void sendError(HttpServletResponse response, String message, int rc) throws IOException {
		BaseResponse<String> baseResponse = new BaseResponse<String>(HttpStatusConverter.intToHttpStatus(rc),
				HttpStatusConverter.intToHttpStatusString(rc), message, null);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(rc);
		OutputStream responseStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(responseStream, baseResponse);
		responseStream.flush();
	}

}
