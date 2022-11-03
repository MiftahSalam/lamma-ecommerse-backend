package com.miftah.lamaecommerse.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(allowGetters = false, allowSetters = false, value = "baseId")
public abstract class BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4408406696858243289L;

	@JsonIgnore
	private Long baseId;

	protected abstract void initSuperId();
}
