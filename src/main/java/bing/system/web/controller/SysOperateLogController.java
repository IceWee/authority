package bing.system.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.domain.GenericPage;
import bing.domain.LabelValueBean;
import bing.system.condition.SysOperateLogCondition;
import bing.system.model.SysOperateLog;
import bing.system.service.SysOperateLogService;
import bing.system.vo.SysOperateLogVO;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysOperateLogController extends GenericController {

	private static final String PREFIX = "system/log";
	private static final String AJAX_LOG_LIST = "ajax/system/log/list";

	private static final String LIST = PREFIX + "/list";

	@Autowired
	private SysOperateLogService sysOperateLogService;

	@ModelAttribute("operateTypes")
	protected List<LabelValueBean> operateTypes() {
		List<LabelValueBean> operateTypes = new ArrayList<>();
		operateTypes.add(new LabelValueBean("增加", String.valueOf(SysOperateLog.OPERATE_ADD)));
		operateTypes.add(new LabelValueBean("修改", String.valueOf(SysOperateLog.OPERATE_MODIFY)));
		operateTypes.add(new LabelValueBean("删除", String.valueOf(SysOperateLog.OPERATE_DELETE)));
		operateTypes.add(new LabelValueBean("其他", String.valueOf(SysOperateLog.OPERATE_OTHER)));
		return operateTypes;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_LOG_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysOperateLogVO>> logs(SysOperateLogCondition condition) {
		RestResponse<GenericPage<SysOperateLogVO>> response = new RestResponse<>();
		GenericPage<SysOperateLogVO> page = sysOperateLogService.listByPage(condition);
		response.setData(page);
		return response;
	}

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

}
