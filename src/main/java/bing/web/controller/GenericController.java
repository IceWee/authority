package bing.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import bing.constant.GlobalConstants;
import bing.constant.MessageKeys;
import bing.constant.StatusEnum;
import bing.domain.LabelValueBean;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;

/**
 * 抽象Controller
 * 
 * @author IceWee
 */
public abstract class GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericController.class);

	@Autowired
	protected MessageSourceService messageSourceService;

	/**
	 * 全局状态列表
	 * 
	 * @return
	 */
	@ModelAttribute("statusList")
	protected List<LabelValueBean> statusList() {
		List<LabelValueBean> statusList = new ArrayList<>();
		statusList.add(new LabelValueBean("正常", String.valueOf(StatusEnum.NORMAL.ordinal())));
		statusList.add(new LabelValueBean("锁定", String.valueOf(StatusEnum.LOCKED.ordinal())));
		return statusList;
	}

	/**
	 * 表单校验是否有错误
	 * 
	 * @param bindingResult
	 * @return
	 */
	protected boolean hasErrors(BindingResult bindingResult) {
		return bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty();
	}

	/**
	 * 表单校验是否有错误
	 * 
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	protected boolean hasErrors(BindingResult bindingResult, Model model) {
		boolean hasErrors = bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty();
		if (hasErrors) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String error = errors.get(0).getDefaultMessage();
			model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_ERROR, error);
		}
		return hasErrors;
	}

	/**
	 * 解析校验错误信息
	 * 
	 * @param bindingResult
	 * @return
	 */
	protected String getError(BindingResult bindingResult) {
		String error = StringUtils.EMPTY;
		List<ObjectError> errors = bindingResult.getAllErrors();
		if (hasErrors(bindingResult)) {
			error = errors.get(0).getDefaultMessage();
			LOGGER.error(error);
		}
		return error;
	}

	/**
	 * 解析异常错误
	 * 
	 * @param e
	 * @return
	 */
	protected String getError(Exception e) {
		String error;
		String code = BusinessExceptionCodes.singleton().SERVER_ERROR;
		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			code = be.getCode();
		}
		error = getMessage(code, BusinessExceptionCodes.singleton().UNKNOW_ERROR);
		LOGGER.error(error);
		return error;
	}

	/**
	 * 将错误信息放到返回Model中供前端页面显示
	 * 
	 * @param e
	 * @param model
	 */
	protected void setError(Exception e, Model model) {
		String error = getError(e);
		addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_ERROR, error);
	}

	/**
	 * 将错误信息放到返回Model中供前端页面显示
	 * 
	 * @param key
	 * @param model
	 */
	protected void setError(String key, Model model) {
		String error = getMessage(key, BusinessExceptionCodes.singleton().UNKNOW_ERROR);
		addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_ERROR, error);
	}

	/**
	 * 将提示信息放到返回Model中供前端页面显示
	 * 
	 * @param key
	 * @param model
	 */
	protected void setMessage(String key, Model model) {
		String message = getMessage(key, MessageKeys.OPERATE_SUCCESS);
		addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_MESSAGE, message);
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param defaultKey 备选
	 * @return
	 */
	protected String getMessage(String key, String defaultKey) {
		String message;
		try {
			message = messageSourceService.getMessage(key);
		} catch (Exception e) {
			LOGGER.warn("国际化文件中未配置消息提示：{}", key);
			message = messageSourceService.getMessage(defaultKey);
		}
		return message;
	}

	/**
	 * 添加返回属性，判断了重定向请求
	 * 
	 * @param model
	 * @param key
	 * @param value
	 */
	protected void addAttribute(Model model, String key, Object value) {
		if (model instanceof RedirectAttributesModelMap) {
			((RedirectAttributesModelMap) model).addFlashAttribute(key, value);
		} else {
			model.addAttribute(key, value);
		}
	}

}
