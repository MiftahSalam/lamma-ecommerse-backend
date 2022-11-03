package com.miftah.lamaecommerse.models;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8388548304079401540L;

	public static final String USER_ROLE = "ROLE_USER";
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

	private String authority;
}
