package bing.system.model;

import bing.domain.GenericObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SysOperateLog extends GenericObject implements Serializable {

    private static final long serialVersionUID = -2336337357792128515L;

    /**
     * 新增操作
     */
    public static final int OPERATE_ADD = 1;

    /**
     * 修改操作
     */
    public static final int OPERATE_MODIFY = 2;

    /**
     * 删除操作
     */
    public static final int OPERATE_DELETE = 3;

    /**
     * 其他操作
     */
    public static final int OPERATE_OTHER = 4;

    /**
     * PK
     */
    private Integer id;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 操作人登录账号
     */
    private Integer operateUserId;

    /**
     * 操作人用户名
     */
    private String operateUserName;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作内容
     */
    private String operateContent;

    public SysOperateLog() {
        super();
    }

    public SysOperateLog(String moduleName, Integer operateType, Integer operateUserId, String operateUserName, String operateContent) {
        this();
        this.moduleName = moduleName;
        this.operateType = operateType;
        this.operateUserId = operateUserId;
        this.operateUserName = operateUserName;
        this.operateContent = operateContent;
    }

    public Date getOperateTime() {
        if (operateTime == null) {
            operateTime = new Date();
        }
        return (Date) operateTime.clone();
    }

    public void setOperateTime(Date operateTime) {
        if (operateTime != null) {
            this.operateTime = (Date) operateTime.clone();
        } else {
            this.operateTime = null;
        }
    }
}