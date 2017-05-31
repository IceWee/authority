package bing.system.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import bing.domain.GenericVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserVO extends GenericVO {

	private static final long serialVersionUID = -7983749899316231902L;

	@Excel(name = "用户名", orderNum = "1")
	private String username;

	@Excel(name = "姓名", orderNum = "2")
	private String name;

	@Excel(name = "手机", orderNum = "3")
	private String mobile;

}
