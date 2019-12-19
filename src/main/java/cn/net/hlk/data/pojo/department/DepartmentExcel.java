package cn.net.hlk.data.pojo.department;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.net.hlk.data.pojo.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 【描述】：组织机构导出导入Bean
 * @author 柴志鹏
 * @date 2019/9/24 0024
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Alias("departmentExcel")
public class DepartmentExcel extends BaseBean implements Serializable, IExcelModel {
    /** 【描述】：主键ID */
    private String id;
    /** 【描述】：父节点ID */
    private String parentId;
    /** 【描述】：机构编码 */
    @Excel(name = "机构编码(*)", orderNum = "1", width = 20, isImportField = "true")
    private String code;
    /** 【描述】：机构名称 */
    @Excel(name = "机构名称(*)", orderNum = "2", width = 60, isImportField = "true")
    private String name;
    /** 【描述】：层级深度 */
    private int depth;
    /** 【描述】：父节点编码 */
    private String parentCode;

    /** 【描述】：自定义errorMsg接受下面重写IExcelModel接口的get和setErrorMsg方法*/
    @Excel(name = "错误信息", orderNum = "3", width = 30)
    private String errorMsg;
}
