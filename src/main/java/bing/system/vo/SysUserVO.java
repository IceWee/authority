package bing.system.vo;

import bing.constant.GenderEnum;
import bing.domain.GenericVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;

@Getter
@Setter
@NoArgsConstructor
public class SysUserVO extends GenericVO {

    private static final long serialVersionUID = -7983749899316231902L;

    @ApiModelProperty(value = "用户名", required = true, dataType = "String", example = "zhangsan")
    @Excel(name = "用户名", orderNum = "1")
    private String username;

    @ApiModelProperty(value = "姓名", required = true, dataType = "String", example = "张三")
    @Excel(name = "姓名", orderNum = "2")
    private String name;

    @ApiModelProperty(value = "手机", dataType = "String", example = "13011112222")
    @Excel(name = "手机", orderNum = "3")
    private String mobile;

    @ApiModelProperty(value = "性别", dataType = "Integer", example = "0")
    private Integer gender;

    @ApiModelProperty(value = "性别文字", dataType = "String", example = "男")
    @Excel(name = "性别", orderNum = "4")
    private String genderText;

    @ApiModelProperty(value = "Email", dataType = "String", example = "peter@126.com")
    @Excel(name = "Email", orderNum = "5")
    private String email;

    public String getGenderText() {
        genderText = StringUtils.EMPTY;
        if (GenderEnum.MALE.ordinal() == getGender()) {
            genderText = "男";
        } else if (GenderEnum.FEMALE.ordinal() == getGender()) {
            genderText = "女";
        }
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public Integer getGender() {
        if (gender == null) {
            gender = GenderEnum.MALE.ordinal();
        }
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SysUserVO other = (SysUserVO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
