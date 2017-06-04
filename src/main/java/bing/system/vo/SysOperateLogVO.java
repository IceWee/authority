package bing.system.vo;

import java.util.Date;

import bing.system.model.SysOperateLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysOperateLogVO {

	private Integer id;

	private String moduleName;

	private Integer operateType;

	private String operateTypeText;

	private Integer operateUserId;

	private String operateUserName;

	private Date operateTime;

	private String operateContent;

	public String getOperateTypeText() {
		if (SysOperateLog.OPERATE_ADD == operateType) {
			operateTypeText = "增加";
		} else if (SysOperateLog.OPERATE_MODIFY == operateType) {
			operateTypeText = "修改";
		} else if (SysOperateLog.OPERATE_DELETE == operateType) {
			operateTypeText = "删除";
		} else {
			operateTypeText = "其他";
		}
		return operateTypeText;
	}

}