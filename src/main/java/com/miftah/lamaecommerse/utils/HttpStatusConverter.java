package com.miftah.lamaecommerse.utils;

import org.springframework.http.HttpStatus;

public class HttpStatusConverter {
	public static String intToHttpStatusString(int rc) {
		if (rc < 200 && rc > 600)
			return null;

		return HttpStatus.valueOf(rc).toString().substring(0, 3);
	}

	public static HttpStatus intToHttpStatus(int rc) {
		if (rc < 200 && rc > 600)
			return null;

		return HttpStatus.valueOf(rc);
	}
}
