package com.miftah.lamaecommerse.exceptions;

import com.miftah.lamaecommerse.dtos.ResponseMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResponseMessage RespMsg;
	private String errorMessage;
	private String serviceCode;

	public BaseException(ResponseMessage code, String errorMessage, String serviceCode) {
		super(errorMessage);
		this.RespMsg = code;
		this.errorMessage = errorMessage;
		this.serviceCode = serviceCode;
	}

}
