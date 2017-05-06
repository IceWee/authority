package bing.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

/**
 * 测试使用，读取权限配置文件，实际项目中应从数据库读取
 * 
 * @author IceWee
 */
public class CustomSecurityContext {

	private static Map<String, Collection<ConfigAttribute>> METADATA_SOURCE_MAP = new HashMap<>();

	static {
		init();
	}

	private CustomSecurityContext() {
		super();
	}

	private static void init() {
		try {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourcePatternResolver.getResources("classpath*:auth.properties");
			if (ArrayUtils.isEmpty(resources)) {
				return;
			}
			Properties properties = new Properties();
			for (Resource resource : resources) {
				properties.load(resource.getInputStream());
			}
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				String[] values = StringUtils.split(value, ",");
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				ConfigAttribute configAttribute = new SecurityConfig(key);
				configAttributes.add(configAttribute);
				for (String v : values) {
					METADATA_SOURCE_MAP.put(StringUtils.trim(v), configAttributes);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Collection<ConfigAttribute>> getMetadataSource() {
		return METADATA_SOURCE_MAP;
	}

}
