package com.miftah.lamaecommerse.exceptions;

import com.miftah.lamaecommerse.dtos.ResponseMessage;

public class AlreadyExistException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyExistException(ResponseMessage code, String errorMessage, String serviceCode) {
		super(code, errorMessage, serviceCode);
	}

}
