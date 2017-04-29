package bing.security;

import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bing.domain.AbstractObject;
import bing.domain.CrudGroups;

/**
 * Spring Security用户
 * 
 * @author IceWee
 */
public class CustomUserDetails extends AbstractObject implements UserDetails {

	private static final long serialVersionUID = 5230111703513005505L;

	@NotBlank(message = "{username.required}")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+$", message = "{username.illegal}")
	@Size(min = 6, max = 32, message = "{username.valid.length}")
	protected String username;

	@NotBlank(groups = {CrudGroups.Create.class}, message = "{password.required}")
	protected String password;

	protected boolean enabled = true;
	protected boolean accountNonExpired = true;
	protected boolean accountNonLocked = true;
	protected boolean credentialsNonExpired = true;

	/**
	 * 权限集合
	 */
	@JsonIgnore
	protected Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

	public CustomUserDetails() {
		super();
	}

	public CustomUserDetails(String username, String password) {
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
