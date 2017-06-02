package bing.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * JSON工具类，底层使用Jackson
 * 
 * @author IceWee
 */
public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			// null - ""
			@Override
			public void serialize(Object obj, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
				generator.writeString("");
			}

		});
	}

	private JsonUtils() {
		super();
	}

	/**
	 * 解析JSON
	 * 
	 * @param json 字符串
	 * @param clazz 转换后实体类，可以是集合类
	 * @param elementClasses，解析集合时传该参数，即集合泛型类
	 * @return
	 */
	public static <T> T parse(String json, Class<T> clazz, Class<?>... elementClasses) {
		JavaType javaType = getJavaType(clazz, elementClasses);
		try {
			return mapper.readValue(json, javaType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将Java对象转换为JSON字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static JavaType getJavaType(Class<?> colectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(colectionClass, elementClasses);
	}

	public static void main(String[] args) {
		System.out.println(new Object().getClass());
	}

}
