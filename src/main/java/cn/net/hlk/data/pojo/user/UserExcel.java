package cn.net.hlk.data.pojo.user;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.net.hlk.data.pojo.base.BaseBean;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 【描述】：EasyPoi 用户导入导出模板
 * @author 柴志鹏
 * @date 2019/8/15 0015
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Alias("userExcel")
public class UserExcel extends BaseBean implements Serializable, IExcelModel {
    private String id;
    /** 【描述】：姓名 */
    @Excel(name = "姓名(*)", orderNum = "1", width = 15, isImportField = "true")
    private String name;
    /** 【描述】：身份证号 */
    @Excel(name = "证件号(*)", orderNum = "2", width = 20, isImportField = "true")
    private String idCard;
    /** 【描述】：警号 */
    @Excel(name = "警号(*)", orderNum = "3", isImportField = "true")
    private String policeCard;
    /** 【描述】：机构编码 */
    @Excel(name = "机构编码(*)", orderNum = "4", width = 20, isImportField = "true")
    private String departmentCode;
    /** 【描述】：机构名称 */
    @Excel(name = "机构名称", orderNum = "5", width = 20)
    private String departmentName;
    /** 【描述】：联系方式 */
    @Excel(name = "联系方式(*)", orderNum = "6", width = 15, isImportField = "true")
    private String contact;
    /** 【描述】：岗位 */
    @Excel(name = "行政身份(*)", orderNum = "7", width = 20, isImportField = "true")
    private String jobName;
    /** 【描述】：用户状态 */
    @Excel(name = "启用状态(*)", orderNum = "8", replace = {"启用_1", "禁用_0"}, isImportField = "true")
    private Integer state;
    /** 【描述】：角色ID */
    private String roleId;
    /** 【描述】：自定义errorMsg接受下面重写IExcelModel接口的get和setErrorMsg方法*/
    @Excel(name = "错误信息", orderNum = "9", width = 20)
    private String errorMsg;

}
