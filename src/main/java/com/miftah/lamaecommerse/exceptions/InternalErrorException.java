package com.miftah.lamaecommerse.exceptions;

import com.miftah.lamaecommerse.dtos.ResponseMessage;

public class InternalErrorException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InternalErrorException(ResponseMessage code, String errorMessage, String serviceCode) {
		super(code, errorMessage, serviceCode);
		// TODO Auto-generated constructor stub
	}

}
