package bing.system.condition;

import org.apache.commons.lang3.StringUtils;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysOperateLogCondition extends GenericCondition {

	private static final String FIRST_TIME = " 00:00:00";
	private static final String LAST_TIME = " 23:59:59";

	private String moduleName;

	private Integer operateType;

	private String operateUserName;

	private String operateTimeBegin;

	private String operateTimeEnd;

	public String getOperateTimeBegin() {
		if (StringUtils.isNotBlank(operateTimeBegin) && !StringUtils.endsWith(operateTimeBegin, FIRST_TIME)) {
			operateTimeBegin += FIRST_TIME;
		}
		return operateTimeBegin;
	}

	public String getOperateTimeEnd() {
		if (StringUtils.isNotBlank(operateTimeEnd) && !StringUtils.endsWith(operateTimeEnd, LAST_TIME)) {
			operateTimeEnd += LAST_TIME;
		}
		return operateTimeEnd;
	}

}
