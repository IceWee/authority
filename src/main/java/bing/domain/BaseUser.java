package bing.domain;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseUser extends BaseDomain implements UserDetails {

	private static final long serialVersionUID = 5230111703513005505L;

	@NotBlank(message = "{required.username}")
	protected String username;

	@NotBlank(message = "{required.password}")
	protected String password;
	protected boolean enabled = true;
	protected boolean accountNonExpired = true;
	protected boolean accountNonLocked = true;
	protected boolean credentialsNonExpired = true;
	protected Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

	public BaseUser() {
		super();
	}

	public BaseUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
