package cn.net.hlk.data.pojo.resource;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.net.hlk.data.pojo.base.BaseBean;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * 【描述】：资源实体
 * @author 柴志鹏
 * @date 2019/8/29 0029
 * @since 1.0.0
 */
@Data
@Alias("resource")
public class Resource extends BaseBean implements Serializable, IExcelModel {
    /** 【描述】：主键ID */
    private String id;
    /** 【描述】：父ID */
    private String parentId;
    /** 【描述】：父编码 */
    @Excel(name = "父节点编码", orderNum = "1", width = 20, isImportField = "true")
    private String parentCode;
    /** 【描述】：名称 */
    @Excel(name = "资源名称", orderNum = "2", width = 20, isImportField = "true")
    private String name;
    /** 【描述】：节点深度 */
    private int depth;
    /** 【描述】：资源编码 */
    @Excel(name = "资源编码", orderNum = "3", width = 20, isImportField = "true")
    private String resourceCode;
    /** 【描述】：说明描述 */
    @Excel(name = "资源说明", orderNum = "4", width = 20)
    private String description;
    /** 【描述】：资源类型 */
    @Excel(name = "资源类型", orderNum = "5", replace = {"模块_900001", "接口_900002", "菜单_900003", "按钮_900004"}, isImportField = "true")
    private String type = "900001";
    /** 【描述】：排序 */
    private int sort;
    /** 【描述】：启用状态 */
    private int state;
    /** 【描述】：应用ID */
    private String appId;
    /** 【描述】：资源子集 */
    private List<Resource> children;
    /** 【描述】：是否为系统应用 */
    private int isApp;
    /** 【描述】：角色名称 */
    private String roleName;
    /** 【描述】：自定义errorMsg接受下面重写IExcelModel接口的get和setErrorMsg方法*/
    @Excel(name = "错误信息", orderNum = "6", width = 20)
    private String errorMsg;

}
