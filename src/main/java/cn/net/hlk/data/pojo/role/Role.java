package cn.net.hlk.data.pojo.role;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.net.hlk.data.pojo.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 【描述】：角色Bean
 * @author 柴志鹏
 * @date 2019/9/16 0016
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Alias("role")
public class Role extends BaseBean implements IExcelModel {
    /** 【描述】：主键ID */
    private String id;
    /** 【描述】：角色名称 */
    @Excel(name = "角色名称(*)", orderNum = "1", isImportField = "true", width = 20)
    private String name;
    /** 【描述】：角色描述 */
    @Excel(name = "角色描述", orderNum = "2", width = 20)
    private String description;
    /** 【描述】：启用状态 */
    @Excel(name = "启用状态(*)", orderNum = "3", replace = {"启用_1", "禁用_0"}, isImportField = "true")
    private Integer state = 1;
    /** 【描述】：应用ID */
    private String appId;
    /** 【描述】：自定义errorMsg接收下面重写IExcelModel接口的get和setErrorMsg方法 */
    @Excel(name = "错误信息", orderNum = "4", width = 20)
    private String errorMsg;
    /** 警员身份证号 */
    @Excel(name = "警员身份证号(关联数据不要修改)", orderNum = "99", isColumnHidden = true)
    private String idCard;
    
}
