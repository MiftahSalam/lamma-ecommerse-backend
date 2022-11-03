package com.miftah.lamaecommerse.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@RequiredArgsConstructor
@Getter
@Setter
public class User extends BaseModel implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1333121276047606535L;

	private static boolean enabled = true;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NonNull
	private String fullname;

	@Column(nullable = false, unique = true)
	@NonNull
	private String username;

	@Column(nullable = false, unique = true)
	@NonNull
	private String email;

	@Column(nullable = false)
	@NonNull
	@JsonIgnore
	private String password;

	@Column(nullable = false)
	private boolean isAdmin = false;

	@Column
	private String role = Role.USER_ROLE;

	@Column()
	private String imgUrl;

	@CreatedDate()
	private Date createdAt;

	@UpdateTimestamp()
	private Date updatedAt;

	public User() {
		username = "";
		email = "";
	}

	@Override
	@PostConstruct
	protected void initSuperId() {
		super.setBaseId(this.id);
	}

	@PrePersist()
	@PreUpdate
	private void hashPassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		this.password = bCryptPasswordEncoder.encode(this.password);
		this.createdAt = new Date();
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + ", username=" + username + ", email=" + email + ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, fullname, getId(), password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(fullname, other.fullname)
				&& Objects.equals(getId(), other.getId()) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return enabled;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return enabled;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		grantedAuthorities.add(new SimpleGrantedAuthority(getRole()));

		return grantedAuthorities;
	}
}
