package com.miftah.lamaecommerse.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.miftah.lamaecommerse.dtos.ResponseMessage;

public class ExceptionsTest {
	@Test
	void AlreadyExistTest() {
		AlreadyExistException exception = new AlreadyExistException(ResponseMessage.ALREADYEXIST, "data already exist",
				"service 1");
		try {
			throw exception;
		} catch (AlreadyExistException e) {
			assertThat(e).isEqualTo(exception);
			assertThat(e.getMessage()).isEqualTo(exception.getMessage());
			assertThat(e.getErrorMessage()).isEqualTo(exception.getErrorMessage());
		}
	}
}
