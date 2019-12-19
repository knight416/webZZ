package cn.net.hlk.data.pojo.role;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 【描述】：角色与资源关联Bean
 * @author 柴志鹏
 * @date 2019/9/27 0027
 * @since 1.0.0
 */
@Data
@Alias("roleResourceRelation")
public class RoleResourceRelation {
    /** 【描述】：角色名称 */
    @Excel(name = "角色名称", orderNum = "1", width = 20)
    private String roleName;
    /** 【描述】：资源编码 */
    @Excel(name = "资源编码", orderNum = "2", width = 20)
    private String resourceCode;
    /** 【描述】：资源名称 */
    @Excel(name = "资源名称", orderNum = "3", width = 20)
    private String resourceName;
}
