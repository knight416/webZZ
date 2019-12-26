package cn.net.hlk.data.pojo.user;

//import org.hibernate.annotations.GenericGenerator;


import java.sql.Timestamp; import java.util.List;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.department.Department;
import cn.net.hlk.data.pojo.department.Group;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 【描 述】：<用户表实体类>
 * 【表 名】：<t_user>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月5日
 */
@Data
@Alias("user")
public class User implements java.io.Serializable {

    // Fields
    /** 【描 述】：serialVersionUID */
	private static final long serialVersionUID = -8033065078639294542L;
	
	/** 【描 述】：主键 */
	private String uid;
    /** 【描 述】：所属机构 */
    private String department;
    /** 【描 述】：图像 */
    private String photo;
	/** 【描 述】：姓名 */
    private String name;
    /** 【描 述】：身份证号 */
    private String idCard;
    /** 【描 述】：密码 */
    private String password;
    /** 【描述】：联系方式*/
    private String contact;
    /** 【描 述】：状态 */
    private Integer state;
    /** 【描 述】：操作时间 */
    private Timestamp updatetime;
    /** 【描 述】：操作人名称 */
    private String updateuser;
    /** 【描 述】：逻辑删除 */
    private Integer visiable;
    /** 【描 述】：扩展字段 */
    private PageData user_message;
    /** 【描 述】：身份 */
    private String job_type;
    /** 【描 述】：用户名 */
    private String username;
    /** 【描 述】：机构代码 */
    private String system_id;
}