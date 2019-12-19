package cn.net.hlk.data.pojo.application;

import cn.net.hlk.data.pojo.base.BaseBean;
import cn.net.hlk.data.pojo.role.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 【描述】：系统应用POJO
 * @author 柴志鹏
 * @date 2019/8/28 0028
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Alias("application")
public class Application extends BaseBean {
    /** 【描述】：主键ID (UUID) */
    private String id;
    /** 【描述】：应用名称 */
    private String name;
    /** 【描述】：应用唯一编码 */
    private String code;
    /** 【描述】：应用地址 */
    private String address;
    /** 【描述】：应用IP */
    private String ip;

    /** 【描述】：角色信息 */
    private List<Role> roles;

}
