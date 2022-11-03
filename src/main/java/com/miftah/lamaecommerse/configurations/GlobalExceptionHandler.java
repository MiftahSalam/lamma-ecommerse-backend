package com.miftah.lamaecommerse.configurations;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.miftah.lamaecommerse.dtos.BaseResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler()
	public ResponseEntity<BaseResponse<String>> handlerAccessDeniedError(HttpServletRequest request,
			AccessDeniedException e) {
		BaseResponse<String> baseResponse = new BaseResponse<String>(HttpStatus.FORBIDDEN, "403", e.getMessage(), null);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(baseResponse);
	}
}