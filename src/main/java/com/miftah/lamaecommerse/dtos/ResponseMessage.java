package com.miftah.lamaecommerse.dtos;

public enum ResponseMessage {
	SUCCESS("200", "Successful"), BAD_REQUEST("400", "Bad Request"),
	INVALID_FIELD_FORMAT("400", "Invalid Field Format"), UNAUTHORIZED("401", "Unauthorized"),
	NOTFOUND("401", "Not Found"), INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
	ALREADYEXIST("400", "Data Already Exist (Duplicate Not Allowed)");

	public final String prefixCode;
	public String message;

	private ResponseMessage() {
		this.prefixCode = "";
		this.message = "";
	}

	private ResponseMessage(String prefixCode, String message) {
		this.prefixCode = prefixCode;
		this.message = message;
	}

	public String getResponseCode(String serviceCode) {
		return this.prefixCode + serviceCode;
	}

	public static ResponseMessage resolve(String prefixCode, String suffixCode) {
		for (ResponseMessage en : values()) {
			if (en.prefixCode.equals(prefixCode))
				return en;
		}
		return null;
	}

}
