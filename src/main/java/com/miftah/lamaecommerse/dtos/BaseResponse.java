package com.miftah.lamaecommerse.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
	private HttpStatus status;
	private String code = "200";
	private String message = "Success";
	private T data;

	public BaseResponse(HttpStatus status, String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
		this.status = status;
	}

	public BaseResponse() {
	}

	public void set(String message, String code) {
		this.code = code;
		this.message = message;
	}

	public ResponseEntity<BaseResponse<T>> buildResponse() {
		return new ResponseEntity<BaseResponse<T>>(this, this.status);
	}
}
