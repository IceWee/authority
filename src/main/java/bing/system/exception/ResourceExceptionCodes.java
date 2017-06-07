package bing.system.exception;

import bing.exception.BusinessExceptionCodes;
import io.swagger.annotations.ApiModelProperty;

public class ResourceExceptionCodes extends BusinessExceptionCodes {

	private static class SingletonHolder {
		private static final ResourceExceptionCodes INSTANCE = new ResourceExceptionCodes();
	}

	/**
	 * 资源异常错误编码前缀
	 */
	private static final String PREFIX = "RESOURCE_";

	/**
	 * Url已被使用
	 */
	@ApiModelProperty(value = "Url已被使用", dataType = "String", required = true, position = 1, example = "RESOURCE_001")
	public final String URL_USED = PREFIX + "001";

	/**
	 * Url禁止修改
	 */
	@ApiModelProperty(value = "Url禁止修改", dataType = "String", required = true, position = 2, example = "RESOURCE_002")
	public final String URL_FORBIDDEN_MODIFY = PREFIX + "002";

	/**
	 * 分类下包含资源禁止删除
	 */
	@ApiModelProperty(value = "分类下包含资源禁止删除", dataType = "String", required = true, position = 3, example = "RESOURCE_003")
	public final String CATEGORY_CONTAINS_RESOURCE = PREFIX + "003";

	/**
	 * 分类下包含子分类禁止删除
	 */
	@ApiModelProperty(value = "分类下包含子分类禁止删除", dataType = "String", required = true, position = 4, example = "RESOURCE_004")
	public final String CATEGORY_CONTAINS_SUBCATEGORY = PREFIX + "004";

	/**
	 * 资源已被菜单使用禁止删除
	 */
	@ApiModelProperty(value = "资源已被菜单使用禁止删除", dataType = "String", required = true, position = 5, example = "RESOURCE_005")
	public final String USED_BY_MENU = PREFIX + "005";

	/**
	 * 资源已授权给角色禁止删除
	 */
	@ApiModelProperty(value = "资源已授权给角色禁止删除", dataType = "String", required = true, position = 6, example = "RESOURCE_006")
	public final String AUTHORIZED_TO_ROLE = PREFIX + "006";

	protected ResourceExceptionCodes() {
		super();
	}

	public static ResourceExceptionCodes singleton() {
		return SingletonHolder.INSTANCE;
	}

}
