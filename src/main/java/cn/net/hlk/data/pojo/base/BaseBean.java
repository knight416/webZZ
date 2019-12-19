/**
 * Copyright (C), 2018-2019, 海邻科信息技术有限公司
 * FileName: BaseBean
 * Author:   柴志鹏
 * Email:    chaizhipeng@hylink.net.cn
 * Date:     2019/8/28 0028 15:37
 * Description: 基础Bean
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.net.hlk.data.pojo.base;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 〈基础Bean〉
 *
 * @author 柴志鹏
 * @date 2019/8/28 0028
 * @since 1.0.0
 */
@Data
public class BaseBean {
    /** 【描述】：操作人名称 */
    private String optName;
    /** 【描述】：操作人ID */
    private String optId;
    /** 【描述】：创建时间 */
    private Timestamp optCreateTime;
    /** 【描述】：操作时间 */
    private Timestamp optModifyTime;
    /** 【描述】：逻辑删除 1可见，0隐藏 */
    private int visiable = 1;

}
