package bing.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * URI与访问当前URI应具备的角色列表(存储的是角色ID集合)
 * 
 * @author IceWee
 */
@Setter
@Getter
@NoArgsConstructor
public class URISecurityConfigs {

	private String uri;

	private Collection<ConfigAttribute> configAttributes = new ArrayList<>();

	public URISecurityConfigs(String uri) {
		super();
		this.uri = uri;
	}

	public void addConfig(Object config) {
		configAttributes.add(new SecurityConfig(Objects.toString(config)));
	}

}
