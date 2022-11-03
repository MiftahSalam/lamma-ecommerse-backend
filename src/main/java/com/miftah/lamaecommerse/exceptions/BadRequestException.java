package com.miftah.lamaecommerse.exceptions;

import com.miftah.lamaecommerse.dtos.ResponseMessage;

public class BadRequestException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadRequestException(ResponseMessage code, String errorMessage, String serviceCode) {
		super(code, errorMessage, serviceCode);
	}

}
